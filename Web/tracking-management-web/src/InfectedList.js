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
        this.updateCheck = props.updateCheck;
    }

    checkboxEventHandler = (e) => {
        const thisUser = this.state.user;
        thisUser.managerCheck = !thisUser.managerCheck;
        this.setState({ user: thisUser});
        this.updateCheck(this.state.user);
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