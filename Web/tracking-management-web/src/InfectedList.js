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
            <table id="list">
                <thead>
                    <tr>
                        <th>UUID</th>
                        <th>확진날짜</th>
                        <th>추정날짜</th>
                        <th>행위</th>
                        <th>check-box</th>
                    </tr>
                </thead>
                <tbody>
                    <th>{user.uuid}</th>
                    <th>{user.judgmentDate}</th>
                    <th>{user.estimatedDate}</th>
                    <th>{user.detailSituation}</th>
                    <th><Checkbox checked={user.managerCheck} onChange={this.checkboxEventHandler}/></th>
                </tbody>
            </table>
                    
            <select name="search_data">
                <option value="" selected="selected">선택</option>
                <option name="judgmentDate" value="접촉자 UUID">접촉자 UUID</option>
                <option name="estimatedDate" value="접촉일자">확진날짜</option>
                <option name="first_time" value="첫 접촉시각">추정날짜</option>
                <option name="last_time" value="마지막 접촉시각">마지막 접촉시각</option>
                <option name="habit" value="행위">행위</option>
            </select>

            <input type="text"></input><button name="search">검색</button>
        </ListItem>
        );
    }
}

export default InfectedList;