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
            <table id="list" align="center">
                <thead>
                    <tr>
                        <th>UUID</th>
                        <th>확진날짜</th>
                        <th>추정날짜</th>
                        <th>행위</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{user.uuid}</td>
                        <td>{user.judgmentDate}</td>
                        <td>{user.estimatedDate}</td>
                        <td>{user.detailSituation}</td>
                        <td><Checkbox checked={user.managerCheck} onChange={this.checkboxEventHandler}/></td>
                    </tr>
                </tbody>
            </table>
        </ListItem>
        );
    }
}

export default InfectedList;