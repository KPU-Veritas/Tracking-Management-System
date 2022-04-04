import React from "react";
import {
    ListItem,
    Checkbox,
  } from "@material-ui/core";
import { call } from "./service/ApiService";

class InfectedList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: props.notice };
    }

    checkboxEventHandler = (e) => {
        if(this.state.user.managerCheck == 0) {
            this.state.user.managerCheck = !this.state.user.managerCheck;
            const id = this.state.user.id;
            const managerCheck = this.state.user.managerCheck;
            call("/system/check", "PUT", { id : id, managerCheck : managerCheck } );
        }
    };

    render() {
        const user = this.state.user;
        return (

        <ListItem>
            <Checkbox checked={user.managerCheck} onChange={this.checkboxEventHandler}/>
              <ul>uuid : {user.uuid}, 확진날짜 : {user.judgmentDate}, 추정날짜 : {user.estimatedDate}, 행위 : {user.detailSituation} </ul>
        </ListItem>
        );
    }
}

export default InfectedList;