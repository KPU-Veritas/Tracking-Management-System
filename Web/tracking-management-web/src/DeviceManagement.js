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
        window.location.replace("/")
    }

    render() {
        const device = this.state.device;
        return (
            <Container>
                <ul>아이디 : {device.id}, 장소 : {device.place} {<Button onClick={this.deleteDevice}>삭제</Button>}</ul>
            </Container>
            
        );
    }
}

export default DeviceManagement;