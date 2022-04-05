import React from "react";
import {
    Button,
    Container,
  } from "@material-ui/core";
import { call, } from "./service/ApiService";

class UserList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { user: props.user };
    }

    pushNotification = () => {
        const uuid = this.state.user.uuid;
        call("/system/notificateindividual", "POST", {uuid : uuid});
        alert("해당 사용자 모바일 디바이스로 알림을 보냈습니다.");
    }

    render() {
        const user = this.state.user;
        return (
            <Container>
            <ul>이름 : {user.username}, 이메일 : {user.email}, 전화번호 : {user.phoneNumber}, 주소 : {user.detailAddress} {<Button onClick={this.pushNotification}>알림</Button>}</ul>
            </Container>
        );
    }
}

export default UserList;