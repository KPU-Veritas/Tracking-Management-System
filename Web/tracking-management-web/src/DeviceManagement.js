import React from "react";
import {
    Button,
    Container,
  } from "@material-ui/core";
import { call, } from "./service/ApiService";

class DeviceManagement extends React.Component {
    constructor(props) {
        super(props);
        this.state = { device: props.device };
    }

    deleteDevice = () => {
        const id = this.state.device.id;
        const place = this.state.device.place;
        call("/system/deletedevice", "POST", {id : id, place : place});
        window.location.href = "/main";
    }

    render() {
        const device = this.state.device;
        return (
                <Container>
                    <table id="list" align="center">
                        <thead>
                            <tr>
                                <th>아이디</th>
                                <th>장소</th>
                                <th></th>  
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{device.id}</td>
                                <td>{device.place}</td>
                                <td>{<Button onClick={this.deleteDevice}>삭제</Button>}</td>
                            </tr>
                        </tbody>
                    </table>
                        
                        <br></br>
            
                </Container>
        );
    }
}

export default DeviceManagement;