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
                    <th>알림</th>
                </tr>
            </thead>
            <tbody>
                <th>{user.username}</th>
                <th>{user.email}</th>
                <th>{user.phoneNumber}</th>
                <th>{user.detailAddress}</th>
                <th>{<Button onClick={this.pushNotification}>알림</Button>}</th>
            </tbody>
        </table>
            
            <br></br>
            
            <select name="search_data">
                <option value="" selected="selected">선택</option>
                <option name="name" value="이름">이름</option>
                <option name="email" value="회사원">E-Mail</option>
                <option name="pnumber" value="전화번호">전화번호</option>
                <option name="address" value="주소">주소</option>
            </select>

            <input type="text"></input><button name="search">검색</button> 
            </Container>
            
        );
    }
}

export default UserList;