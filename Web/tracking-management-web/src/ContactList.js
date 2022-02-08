import React from "react";

class ContactList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { contact: props.contact };
    }

    render() {
        const contact = this.state.contact;
        return (
            <ul>이용자ID : {contact.id}, 접촉자uuid : {contact.contactTargetUuid}, 접촉일자 : {contact.date}, 첫 접촉시각 : {contact.firstTime}, 마지막 접촉시각 : {contact.lastTime}</ul>
        );
    }
}

export default ContactList;