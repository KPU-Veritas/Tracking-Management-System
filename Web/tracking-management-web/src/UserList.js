import React from "react";
import {
    ListItem,
    ListItemText,
    InputBase,
  } from "@material-ui/core";

class UserList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { user: props.user };
    }

    render() {
        const user = this.state.user;
        <p>유저리스트임.</p>
        return (
            <ul>{user.username}, {user.email}, {user.phoneNumber}, {user.detailAddress}</ul>
        );
    }
}

export default UserList;