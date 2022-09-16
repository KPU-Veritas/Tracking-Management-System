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

    render() {
        const user = this.state.user;
        const link = './notification/' + user.email;
        const link2 = './addinfected/' + user.uuid;
        const link3 = './modifyuser/' + user.uuid + '/' + user.email + '/' + user.username + '/' + user.phoneNumber + '/' + user.simpleAddress + '/' + user.detailAddress;
        return (
        <Container>
        
        <table id="list" align="center">
            <thead>
                <tr align="left">
                    <th>E-Mail&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</th>
                    <th>이름&emsp;&emsp;&emsp;</th>
                    <th>전화번호&emsp;&emsp;&emsp;&emsp;</th>
                    <th>주소&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</th>
                    <th>위험도</th>
                    <th></th>
                    <th></th>
                    <th></th>    
                </tr>
            </thead>
            <tbody>
                <tr align="left">
                    <td>{user.email}</td>
                    <td>{user.username}</td>
                    <td>{user.phoneNumber}</td>
                    <td>{user.simpleAddress}</td>
                    <td>{user.risk}</td>
                    <td>{<Link to={link}>
                    <Button>경고전송</Button></Link>}</td>
                    <>
                    <td>{<Link to={link2}>
                    <Button>확진</Button></Link>}</td>
                    <td>{<Link to={link3}>
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