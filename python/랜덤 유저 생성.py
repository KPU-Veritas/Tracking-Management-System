# made by 정회민
# rest API 사용해서 유저 생성 및 접촉 기록 생성
# 사용하기 전 user_entity, contact 테이블 drop
# 서버 재시작 후 실행

from random import *
import requests
import json
import string

ipV4 = "http://localhost:8080/"
signupPath = "auth/signup/"
signinPath = "auth/signin/"
recordContactPath = "contact/recordcontact/"
headers = {'Content-Type': 'application/json; charset=utf-8;'}

# 회원가입
firstNames = ["김", "이", "박", "조", "정", "남궁", "재갈", "채", "문", "한", "최", "류", "구", "방", "지", "주", "백", "하", "안", "도", "서",
              "성", "염", "엄"]
lastNames = ["회민", "진", "성은", "한결", "준연", "범준", "동현", "민규", "성원", "채영", "세은", "준식", "공명", "수정", "수진", "민호", "민영", "혜미",
             "민정", "준하", "해미", "순재"]
emails = ["@google.com", "@daum.net", "@naver.com", "@tukorea.ac.kr"]
alphabet = string.ascii_lowercase
digits = string.digits

# uuid List
users = []


class User:
    def __init__(self, username, email, password, phoneNumber, simpleAddress, detailAddress):
        self.uuid = None
        self.token = None
        self.username = username
        self.email = email
        self.password = password
        self.phoneNumber = phoneNumber
        self.simpleAddress = simpleAddress
        self.detailAddress = detailAddress

    def setUuid(self, uuid):
        self.uuid = uuid

    def setToken(self, token):
        self.token = token

    def getSigninData(self) -> dict:
        data = {
            "email": self.email,
            "password": self.password
        }
        return data

    def getSignupData(self) -> dict:
        data = {
            "username": self.username,
            "email": self.email,
            "password": self.password,
            "phoneNumber": self.phoneNumber,
            "simpleAddress": self.simpleAddress,
            "detailAddress": self.detailAddress
        }
        return data

    def __str__(self):
        return "email:{0}\npassword:{1}\nuuid:{2}\ntoken:{3}".format(self.email, self.password, self.uuid, self.token)


def createUser() -> User:
    username = choice(firstNames) + choice(lastNames)

    email = ""
    n = randrange(4, 8)
    for _ in range(n):
        email += choice(alphabet + digits)
    email += choice(emails)

    password = ""
    n = randrange(8, 13)
    for i in range(n):
        password += choice(alphabet + digits)

    phoneNumber = "010-"
    for _ in range(4):
        phoneNumber += choice(digits)
    phoneNumber += "-"
    for _ in range(4):
        phoneNumber += choice(digits)

    simpleAddress = "경기 시흥시 서촌상가4길 17(화신오피스텔)"
    detailAddress = choice(["A", "B"]) + "동 " + choice(digits) + choice(digits) + choice(digits) + "호"
    user = User(username, email, password, phoneNumber, simpleAddress, detailAddress)

    return user


def postSignUp(user: User):
    url = ipV4 + signupPath
    request = requests.post(url, data=json.dumps(user.getSignupData()), headers=headers)
    response = json.loads(request.text)
    user.setUuid(response['uuid'])
    users.append(user)


def postSignIn(user: User):
    url = ipV4 + signinPath
    request = requests.post(url, data=json.dumps(user.getSigninData()), headers=headers)
    response = json.loads(request.text)
    user.setToken(response['token'])


def postRecordContact(user1: User, user2: User, data: dict):
    url = ipV4 + recordContactPath
    userHeaders = {
        'Content-Type': 'application/json; charset=utf-8;',
        'Authorization': 'Bearer ' + user1.token
    }
    request = requests.post(url, data=json.dumps(data), headers=userHeaders)

    uuid = data['uuid']
    contactTargetUuid = data['contactTargetUuid']
    data['uuid'] = contactTargetUuid
    data['contactTargetUuid'] = uuid

    userHeaders['Authorization'] = 'Bearer ' + user2.token
    request = requests.post(url, data=json.dumps(data), headers=userHeaders)


def getContactData() -> (User, User, dict):
    user1 = choice(users)
    while True:
        user2 = choice(users)
        if user1.uuid != user2.uuid:
            break

    day = str(choice([0, 1, 2, 3]))
    if day == '0':
        day += str(randrange(1, 10))
    elif day == '3':
        day += '0'
    else:
        day += str(choice(digits))
    date = '22-04-' + day

    first = randrange(0, 60 * 60 * 24)
    last = randrange(first+1, 60 * 60 * 24)

    contactTime = randrange(1, last-first)
    h = first // 60 // 60
    first -= h * 60 * 60
    m = first // 60
    s = first - m * 60
    if len(str(h)) == 1:
        firstTime = '0'+str(h)+':'
    else:
        firstTime = str(h) + ':'

    if len(str(m)) == 1:
        firstTime += '0' + str(m) + ':'
    else:
        firstTime += str(m) + ':'

    if len(str(s)) == 1:
        firstTime += '0' + str(s)
    else:
        firstTime += str(s)

    h = last // 60 // 60
    last -= h * 60 * 60
    m = last // 60
    s = last - m * 60
    if len(str(h)) == 1:
        lastTime = '0' + str(h) + ':'
    else:
        lastTime = str(h) + ':'

    if len(str(m)) == 1:
        lastTime += '0' + str(m) + ':'
    else:
        lastTime += str(m) + ':'

    if len(str(s)) == 1:
        lastTime += '0' + str(s)
    else:
        lastTime += str(s)

    checked = 0

    data = {
        "uuid": user1.uuid,
        "contactTargetUuid": user2.uuid,
        "date": date,
        "firstTime": firstTime,
        "lastTime": lastTime,
        "checked": checked,
        "contactTime": contactTime
    }

    return user1, user2, data


def main():
    num = int(input("몇 명 만들까? : "))
    contact = int(input("몇 개 접촉 만들까? : "))

    print(" >> 사용자 생성 중...")
    for i in range(num):
        postSignUp(createUser())
    print(" >> 사용자 생성 완료")

    print(" >> 사용자 token 설정 중...")
    for user in users:
        postSignIn(user)
    print(" >> 사용자 token 설정 완료")

    print(" >> 접촉 기록 생성 중...")
    for i in range(contact):
        postRecordContact(*getContactData())
    print(" >> 접촉 기록 생성 완료")

    for user in users:
        print("------------------------------------------------------------------------------------")
        print(user)
    print("------------------------------------------------------------------------------------")


main()
