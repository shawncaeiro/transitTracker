import urllib2
import json

API_BASE="http://shawnc.us/api/tracker/"

def lambda_handler(event, context):
    if (event["session"]["application"]["applicationId"] !=
            "SKILL_ID"):
        raise ValueError("Invalid Application ID")
    
    if event["session"]["new"]:
        on_session_started({"requestId": event["request"]["requestId"]}, event["session"])

    if event["request"]["type"] == "LaunchRequest":
        return on_launch(event["request"], event["session"])
    elif event["request"]["type"] == "IntentRequest":
        return on_intent(event["request"], event["session"])
    elif event["request"]["type"] == "SessionEndedRequest":
        return on_session_ended(event["request"], event["session"])

def on_session_started(session_started_request, session):
    print "Starting new session."

def on_launch(launch_request, session):
    return get_welcome_response()

def on_intent(intent_request, session):
    intent = intent_request["intent"]
    intent_name = intent_request["intent"]["name"]

    if intent_name == "GetTrainTimes":
        return get_bus_times(intent)
    elif intent_name == "AMAZON.HelpIntent":
        return get_welcome_response()
    elif intent_name == "AMAZON.CancelIntent" or intent_name == "AMAZON.StopIntent":
        return handle_session_end_request()
    else:
        raise ValueError("Invalid intent")

def on_session_ended(session_ended_request, session):
    print "Ending session."
    # Cleanup goes here...

def handle_session_end_request():
    card_title = "BART - Thanks"
    speech_output = "Thank you for using the BART skill.  See you next time!"
    should_end_session = True

    return build_response({}, build_speechlet_response(card_title, speech_output, None, should_end_session))

def get_welcome_response():
    session_attributes = {}
    card_title = "CTA Transit Tracking"
    speech_output = "Welcome to the Alexa CTA times skill. " \
                    "Check to see when the next CTA bus is arriving."
    reprompt_text = "Please ask me for trains times from a station," \
                    "for example Fremont."
    should_end_session = False

    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, reprompt_text, should_end_session))

def get_station_code(route_number):
    mapping = { '77': '4996',
                '36': '5366'}
    return mapping[route_number]

def get_bus_times(intent):
    session_attributes = {}
    card_title = "BART Departures"
    speech_output = "I'm not sure which station you wanted train times for. " \
                    "Please try again."
    reprompt_text = "I'm not sure which station you wanted train times for. " \
                    "Try asking about Fremont or Powell Street for example."
    should_end_session = False

    if "Bus" in intent["slots"]:
        route_number = intent["slots"]["Bus"]["value"]
        station_code = get_station_code(route_number)

        if (route_number != "unkn"):
            card_title = "CTA Bus departures from " + route_number + " bus"

            response = urllib2.urlopen(API_BASE + "route/" + route_number + "/stop/" + station_code)
            data = json.load(response)['data'][0] 
            
            if len(data["arrivalTimes"]) > 0:
                speech_output = "The next " + data['routeName'] + " bus will arrive to " + data['stopName'] + " in "
                for arrival in data["arrivalTimes"]:
                    if data["arrivalTimes"] == "DUE":
                        data["arrivalTimes"] = 0
                    speech_output += arrival + ", " 

                speech_output += "minutes."
                reprompt_text = ""
            else:
                speech_output = "No " + data['routeName'] + " buses are arriving at " + data['stopName'] + " in the near time future."

    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, reprompt_text, should_end_session))

def build_speechlet_response(title, output, reprompt_text, should_end_session):
    return {
        "outputSpeech": {
            "type": "PlainText",
            "text": output.replace("&", "and")
        },
        "card": {
            "type": "Simple",
            "title": title,
            "content": output
        },
        "reprompt": {
            "outputSpeech": {
                "type": "PlainText",
                "text": reprompt_text
            }
        },
        "shouldEndSession": should_end_session
    }

def build_response(session_attributes, speechlet_response):
    return {
        "version": "1.0",
        "sessionAttributes": session_attributes,
        "response": speechlet_response
    }