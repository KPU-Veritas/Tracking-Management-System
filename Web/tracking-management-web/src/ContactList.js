import React from "react";

class ContactList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { contact: props.contact };
    }

    render() {
        const contact = this.state.contact;
        return (
            <ul>이용자ID : {contact.id}, 접촉자uuid : {contact.contactTargetUuid}, 접촉시각 : {contact.dateTime}</ul>
        );
    }
}

export default ContactList;