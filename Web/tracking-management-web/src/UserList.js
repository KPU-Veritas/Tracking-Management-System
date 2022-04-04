import React from "react";

class UserList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { user: props.user };
    }

    render() {
        const user = this.state.user;
        return (
            <ul>이름 : {user.username}, 이메일 : {user.email}, 전화번호 : {user.phoneNumber}, 주소 : {user.detailAddress}</ul>
        );
    }
}

export default UserList;