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
        <table id="list">
            <thead>
                <tr>
                    <th>이름</th>
                    <th>E-Mail</th>
                    <th>전화번호</th>
                    <th>주소</th>
                    <th></th>    
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    <td>{user.phoneNumber}</td>
                    <td>{user.detailAddress}</td>
                    <td>{<Button onClick={this.pushNotification}>알림</Button>}</td>
                </tr>
            </tbody>
        </table>
            
            <br></br>

            </Container>
            
        );
    }
}

export default UserList;