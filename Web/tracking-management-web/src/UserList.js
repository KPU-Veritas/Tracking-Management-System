import React from "react";
import {
    Button,
    Container,
  } from "@material-ui/core";
import { call, } from "./service/ApiService";
import { Link } from 'react-router-dom';


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
        const link = './addinfected/' + user.uuid;
        const link2 = './modifyuser/' + user.uuid + '/' + user.email + '/' + user.username + '/' + user.phoneNumber + '/' + user.simpleAddress + '/' + user.detailAddress;
        return (
        <Container>
        <table id="list" align="center">
            <thead>
                <tr align="left">
                    <th>&nbsp;&nbsp;&nbsp;E-Mail&nbsp;&nbsp;&nbsp;</th>
                    <th>&nbsp;&nbsp;&nbsp;이름&nbsp;&nbsp;&nbsp;</th>
                    <th>&nbsp;&nbsp;&nbsp;전화번호&nbsp;&nbsp;&nbsp;</th>
                    <th>&nbsp;&nbsp;&nbsp;주소&nbsp;&nbsp;&nbsp;</th>
                    <th>&nbsp;&nbsp;&nbsp;위험도&nbsp;&nbsp;&nbsp;</th>
                    <th></th>
                    <th></th>
                    <th></th>    
                </tr>
            </thead>
            <tbody>
                <tr align="center">
                    <td>{user.email}&nbsp;&nbsp;&nbsp;</td>
                    <td>{user.username}&nbsp;&nbsp;&nbsp;</td>
                    <td>{user.phoneNumber}&nbsp;&nbsp;&nbsp;</td>
                    <td>{user.simpleAddress}&nbsp;&nbsp;&nbsp;</td>
                    <td>{user.risk}&nbsp;&nbsp;&nbsp;</td>
                    <td>{<Button onClick={this.pushNotification}>경고전송</Button>}</td>
                    <>
                    <td>{<Link to={link}>
                    <Button>확진</Button></Link>}</td>
                    <td>{<Link to={link2}>
                    <Button>정보수정</Button></Link>}</td>
                    </>
                </tr>
            </tbody>
        </table>
            
            <br></br>

        </Container>
            
        );
    }
}

export default UserList;