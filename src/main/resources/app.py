from flask import Flask, request, session, g, render_template, redirect, url_for
from flask_restful import Resource, Api, reqparse
app = Flask(__name__)
api = Api(app)
app.secret_key = "fAv?\OH???fY\0"
import requests
import boto3
import os
import pdfkit
import uuid
import imgkit
from string import Template
from flask import Flask, render_template
CLOQ_IMAGE = "https://s3.us-east-2.amazonaws.com/rcs-demo/rcs-icons/cloqlogo.jpg"
DOG_IMAGE = "https://s3.amazonaws.com/rcs-barcelona-demo/rcs-pics/dog.png"
def fetchVehicleApis(name, gender, season, brand):
    header = {
        'x-api-key': 'siKDRfghmqalTob5QSr5I3FCyLVajPmK6S5KhFru',
    }
    data = [
    ('project', 'cloq'),
    ('request[callback_url]', 'https://mwc-2019.herokuapp.com/api/messages/video/cloq'),
    ('request[data][first_name]', name),
    ('request[data][type]', 'MMS'),
    ('request[data][season]', season),
    ('request[data][department]', gender),
    ('request[data][brand]', brand),
    ('request[priority]', '1'),
    ]
    response = requests.post('https://vpe-api.vhcl.co/v1/requests', headers=header, data=data)
    print ("Vehicle API submitted to")
    print (response.json())
    return response.json()['id'] , response.json()["asset_url"]

def fetchVehicleApis_coffee(name,  season):
    header = {
         'x-api-key': 'siKDRfghmqalTob5QSr5I3FCyLVajPmK6S5KhFru',
     }
    data = [
     ('project', 'barkers-coffee-demo'),
     ('request[callback_url]', 'https://mwc-2019.herokuapp.com/api/messages/video/coffee'),
     ('request[data][first_name]', name),
     ('request[data][type]', 'MMS'),
     ('request[data][season]', season),
     ('request[priority]', '1'),
     ]
    response = requests.post('https://vpe-api.vhcl.co/v1/requests', headers=header, data=data)
    print ("Vehicle API submitted to for Coffee")
    print (response.json())
    return response.json()['id'] , response.json()["asset_url"]

def htmlFormatCLOQ():
    return """\
        <!doctype html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>A simple, clean, and responsive HTML invoice template</title>

            <style>
            .invoice-box {
                max-width: 800px;
                margin: auto;
                padding: 30px;
                border: 1px solid #eee;
                box-shadow: 0 0 10px rgba(0, 0, 0, .15);
                font-size: 16px;
                line-height: 24px;
                font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
                color: #555;
            }

            .invoice-box table {
                width: 100%;
                line-height: inherit;
                text-align: left;
            }

            .invoice-box table td {
                padding: 5px;
                vertical-align: top;
            }

            .invoice-box table tr td:nth-child(2) {
                text-align: right;
            }

            .invoice-box table tr.top table td {
                padding-bottom: 20px;
            }

            .invoice-box table tr.top table td.title {
                font-size: 45px;
                line-height: 45px;
                color: #333;
            }

            .invoice-box table tr.information table td {
                padding-bottom: 40px;
            }

            .invoice-box table tr.heading td {
                background: #eee;
                border-bottom: 1px solid #ddd;
                font-weight: bold;
            }

            .invoice-box table tr.details td {
                padding-bottom: 20px;
            }

            .invoice-box table tr.item td{
                border-bottom: 1px solid #eee;
            }

            .invoice-box table tr.item.last td {
                border-bottom: none;
            }

            .invoice-box table tr.total td:nth-child(2) {
                border-top: 2px solid #eee;
                font-weight: bold;
            }

            @media only screen and (max-width: 600px) {
                .invoice-box table tr.top table td {
                    width: 100%;
                    display: block;
                    text-align: center;
                }

                .invoice-box table tr.information table td {
                    width: 100%;
                    display: block;
                    text-align: center;
                }
            }

            /** RTL **/
            .rtl {
                direction: rtl;
                font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
            }

            .rtl table {
                text-align: right;
            }

            .rtl table tr td:nth-child(2) {
                text-align: left;
            }
            </style>
        </head>

        <body>
            <div class="invoice-box">
                <table cellpadding="0" cellspacing="0">
                    <tr class="top">
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td class="title">
                                        <img src="https://s3.us-east-2.amazonaws.com/rcs-demo/rcs-icons/cloqlogo.jpg" style="width:100%; max-width:300px;">
                                    </td>

                                    <td>
                                        Invoice #: 123<br>
                                        Created: Feburary 22, 2019<br>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr class="information">
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td>
                                        CLX Communications.<br>
                                        7000 Central Pkwy #1480<br>
                                        Atlanta, GA, 30328
                                    </td>

                                    <td>
                                        Customer <br>
                                        Thong Tran<br>
                                        thong@clxcommunications.com
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr class="heading">
                        <td>
                            Payment Method
                        </td>

                        <td>
                            Check #
                        </td>
                    </tr>

                    <tr class="details">
                        <td>
                            Debit Card (Chase Bank) ****-****-****-4293
                        </td>

                        <td>
                            1000
                        </td>
                    </tr>

                    <tr class="heading">
                        <td>
                            Items
                        </td>

                        <td>
                            Price
                        </td>
                    </tr>
                    $row

                    <tr class="total">
                        <td></td>

                        <td>
                        Total: $price
                        </td>
                    </tr>
                </table>
            </div>
        </body>
        </html>"""
def htmlFormatTxt():
    return """\
            <!doctype html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>A simple, clean, and responsive HTML invoice template</title>

            <style>
            .invoice-box {
                max-width: 800px;
                margin: auto;
                padding: 30px;
                border: 1px solid #eee;
                box-shadow: 0 0 10px rgba(0, 0, 0, .15);
                font-size: 16px;
                line-height: 24px;
                font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
                color: #555;
            }

            .invoice-box table {
                width: 100%;
                line-height: inherit;
                text-align: left;
            }

            .invoice-box table td {
                padding: 5px;
                vertical-align: top;
            }

            .invoice-box table tr td:nth-child(2) {
                text-align: right;
            }

            .invoice-box table tr.top table td {
                padding-bottom: 20px;
            }

            .invoice-box table tr.top table td.title {
                font-size: 45px;
                line-height: 45px;
                color: #333;
            }

            .invoice-box table tr.information table td {
                padding-bottom: 40px;
            }

            .invoice-box table tr.heading td {
                background: #eee;
                border-bottom: 1px solid #ddd;
                font-weight: bold;
            }

            .invoice-box table tr.details td {
                padding-bottom: 20px;
            }

            .invoice-box table tr.item td{
                border-bottom: 1px solid #eee;
            }

            .invoice-box table tr.item.last td {
                border-bottom: none;
            }

            .invoice-box table tr.total td:nth-child(2) {
                border-top: 2px solid #eee;
                font-weight: bold;
            }

            @media only screen and (max-width: 600px) {
                .invoice-box table tr.top table td {
                    width: 100%;
                    display: block;
                    text-align: center;
                }

                .invoice-box table tr.information table td {
                    width: 100%;
                    display: block;
                    text-align: center;
                }
            }

            /** RTL **/
            .rtl {
                direction: rtl;
                font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
            }

            .rtl table {
                text-align: right;
            }

            .rtl table tr td:nth-child(2) {
                text-align: left;
            }

			table, td, th {
             border: 1px solid black;
            }

            table {
             width: 100%;
			 text-align: left;
             }

            th {
             height: 10px;
			 padding: 15px;
			 background-color: #4CAF50;
             color: white;
            }



          </style>
        </head>

        <body>
            <div class="invoice-box">
                <table cellpadding="0" cellspacing="0">
                    <tr class="top">
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td class="title">
                                        <img src="https://s3.amazonaws.com/rcs-barcelona-demo/rcs-pics/dog.png" style="width:100%; max-width:300px;">
                                    </td>

                                    <td>
                                        Order Summary<br>
                                        Created at: Feb 24th, 2019
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr class="information">
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td>
                                        CLX Communications.<br>
                                        7000 Central Pkwy #1480<br>
                                        Atlanta, GA, 30328
                                    </td>

                                    <td>
                                        Customer <br>
                                        Thong Tran Tran<br>
                                        Thong Tran@clxcommunications.com
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>


                     <table class="main">
                       <tr>
                         <th>Item</th>
                         <th>Size</th>
                         <th>Price</th>
                       </tr>
                        $row
                </table>
            </div>
        </body>
        </html>
"""

import codecs
import os
def generateImage(row, name, demo):
    s3 = boto3.resource(
        's3',
        aws_access_key_id="AKIAJXUEEFPHDBHKKCTQ",
        aws_secret_access_key="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"
    )
    html = ""
    if demo == "Barkers":
        html = htmlFormatTxt()
    else:
        html = htmlFormatCLOQ()
    reciept = Template(html).safe_substitute(row=row)
    f = open("Brick_image.html", "w")
    f.write(reciept)
    f.close()
    fileName = uuid.uuid4().__str__() + ".png"
    # config = imgkit.config(wkhtmltoimage='./bin/wkhtmltoimage')
    imgkit.from_file("Brick_image.html", fileName,{"xvfb": ""})
    # imgkit.from_file("Brick_image.html", fileName)
    with open(fileName, 'rb')as data:
        key = "rcs-reciepts/" + fileName
        s3.Bucket("rcs-barcelona-demo").put_object(Key=key, Body=data,
                                         ACL="public-read", ContentType="image/png")
    os.remove(fileName)
    url_str = "https://s3.amazonaws.com/rcs-barcelona-demo/rcs-reciepts/" + fileName
    return url_str
class Video_Coffee(Resource):
    def post(self):
        data = request.get_json()
        name = data['name']
        season = data['season']
        print (name, season)
        id, url = fetchVehicleApis_coffee(name, season)
        return {
                'id':id,
                "url_video": url
                }
class Video(Resource):
    def post(self):
        data = request.get_json()
        print (data)
        name = data['name']
        gender = data['gender']
        season = data['season']
        brand = 'Columbia'
        id , url = fetchVehicleApis(name, gender, season, brand)
        return {
            'id': id,
            'url_video': url
        }
class Reciept(Resource):
    def post(self):
        data = request.get_json()
        row = ""
        for d in data['userCarts']:
            print (d)
            row += """ <tr>
                        <td>
                            {name}
                        </td>
                        <td>
                            {size}
                        </td>
                        <td>
                                {price}
                        </td>
                    </tr>""".format(name=d['name'], price=d['price'], size = d['size'])
        row += """</table>"""
        row += """
        <tr class="item last">
        <td> Total Price </td>
        <td> {total} </td>
        </tr>
        """.format(total=data['totalPrice'])
        html = generateImage(row, data['username'] ,"Barkers")
        return  {
            "url_reciept": html
        }


class Reciept_CLOQ(Resource):
    def post(self):
        data = request.get_json()
        row =""
        for d in data['userCarts']:
            row += """
                    <tr class="item last">
                        <td>
                        {name}
                        </td>
                        <td>
                        {price}
                        </td>
                    </tr>
            """.format(name=d['name'], price=d['price'])
        html = generateImage(row, data['username'] ,"CLoq")
        return {
                "url_reciept": html
                }

from geopy.geocoders import Nominatim
class Address(Resource):
    def post(self):
        data = request.get_json()
        print (data)
        latitude = data['latitude']
        longitude = data['longitude']
        print (latitude, longitude)
        text = "{x} , {y}".format(x=latitude, y=longitude)
        print (text)
        geolocator = Nominatim(user_agent="specify_your_app_name_here")
        location = geolocator.reverse(text)
        return {
            "address": location.address
        }

import os
#os.environ["GOOGLE_APPLICATION_CREDENTIALS"] ="/home/ubuntu/rcs_mwc_receipt_api/src/CLX-AGENT-d13b9ecc5624.json"
#PROJECT_ID = "clx-agent"
#SESSION_ID = "7ec8b03555ad4394aee871ea6cc49d81"
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] ="/Users/thotra/Desktop/Desktop - ThongTran/CLX/rcs-sdk/src/main/resources/HackGSU-d06d2f025e7e.json"
PROJECT_ID = "hackgsu-pmiecq"
SESSION_ID = "3114023dcca640faac8cfc8b9bef3a84"
def detect_intent_texts(project_id, session_id, text, language_code, phoneNumber):
     """Returns the result of detect intent with texts as inputs.
     Using the same `session_id` between requests allows continuation
     of the conversation."""
     context_short_name = "doesnotmatter"

     context_name = "projects/" + PROJECT_ID + "/agent/sessions/" + SESSION_ID + "/contexts/" + \
               context_short_name.lower()

     import dialogflow_v2 as dialogflow
     parameters = dialogflow.types.struct_pb2.Struct()
     parameters['phoneNumber'] = phoneNumber
     context = dialogflow.types.context_pb2.Context(
             name=context_name,
             lifespan_count = 2,
            parameters = parameters
             )
     query_params_1 = {"contexts":[context]}

     session_client = dialogflow.SessionsClient()

     session = session_client.session_path(project_id, session_id)

     text_input = dialogflow.types.TextInput(
         text=text, language_code=language_code)

     query_input = dialogflow.types.QueryInput(text=text_input)

     response = session_client.detect_intent(
         session=session, query_input=query_input,query_params=query_params_1)
     print (response.query_result)
     return response.query_result.intent.display_name, response.query_result.fulfillment_text
class Diagflow(Resource):
    def post(self):
        data = request.get_json()
        mt = data['text']
        phone = data['phone']
        #intent, fullfill , id = detect_intent_texts(PROJECT_ID, SESSION_ID,mt, "env")
        intent, fullfill = detect_intent_texts(PROJECT_ID, SESSION_ID,mt, "en", phone)
        return {
                "intent": intent,
                "fullfill": fullfill
                }
api.add_resource(Address,"/api/address")
api.add_resource(Diagflow, "/api/diagflow/intent")
api.add_resource(Reciept,"/api/receipt")
api.add_resource(Reciept_CLOQ,"/api/receipt/cloq")
api.add_resource(Video,"/api/video")
api.add_resource(Video_Coffee, "/api/video/coffee")
if __name__ == "__main__":
    app.run(host='0.0.0.0',port=4041)
    #print (generateImage("",""))
