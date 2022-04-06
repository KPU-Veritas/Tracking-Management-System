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
                    
        <select name="search_data">
            <option value="" selected="selected">선택</option>
            <option name="judgmentDate" value="접촉자 UUID">접촉자 UUID</option>
            <option name="estimatedDate" value="접촉일자">확진날짜</option>
            <option name="first_time" value="첫 접촉시각">추정날짜</option>
            <option name="last_time" value="마지막 접촉시각">마지막 접촉시각</option>
            <option name="habit" value="행위">행위</option>
        </select>

        <input type="text"></input><button name="search">검색</button>
    </>          
        
        );
    }
}

export default ContactList;