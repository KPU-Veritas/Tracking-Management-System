import React from "react";


class ContactList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { contact: props.contact };
    }

    render() {
        const contact = this.state.contact;
        return (
            <>
            <table id="list">
                <thead>
                    <tr>
                        <th>이용자 UUID</th>
                        <th>접촉자 UUID</th>
                        <th>접촉일자</th>
                        <th>첫 접촉시각</th>
                        <th>마지막 접촉시각</th>
                    </tr>
                </thead>
                <tbody>
                    <th>{contact.uuid}</th>
                    <th>{contact.contactTargetUuid}</th>
                    <th>{contact.date}</th>
                    <th>{contact.firstTime}</th>
                    <th>{contact.lastTime}</th>
                </tbody>
            </table>
            
            <br></br>
            
            <select name="search_data">
                <option value="" selected="selected">선택</option>
                <option name="user_uuid" value="user_uuid">이용자 UUID</option>
                <option name="contact_uuid" value="contact_uuid">접촉자 UUID</option>
                <option name="date" value="date">접촉일자</option>
                <option name="firstTime" value="firstTime">첫 접촉시각</option>
                <option name="lastTime" value="lastTime">마지막 접촉시각</option>
            </select>

            <input type="text"></input><button name="search">검색</button>
            </>
        );
    }
}

export default UserList;