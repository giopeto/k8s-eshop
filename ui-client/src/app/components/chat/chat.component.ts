import { Component, OnInit } from '@angular/core';

import { ChatService } from '../../services/chat.service';
import { Messages } from '../../models/Messages';
import { EventTypes } from '../../models/EventTypes';
import { Events } from '../../models/Events';
import { Users } from '../../models/Users';
import { LoginService } from '../../services/login.service';

@Component({
	selector: 'app-chat',
	templateUrl: './chat.component.html',
	styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

	public messages: Array<Messages> = [];
	public users: Array<Users> = [];
	constructor(private chatService: ChatService, private loginService: LoginService) {}

	ngOnInit() {
		this.chatService.socketSubject.subscribe(event => {
			if (event.type === EventTypes.CHAT_MESSAGE) {
				this.addToMessages(event.payload);
			} else if (event.type === EventTypes.USER_ADDED) {
				this.users.push(event.payload);
			}
		});

		this.loginService.geCurrentUser().subscribe(user=> this.chatService.setUser(user));		
		
		/* //Test with local user
		this.chatService.setUser({
			id: Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15), 
			email: Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15), 
			password: 'qwe', 
			role: 'ROLE_ADMIN'}); */
				
	}

	sendMessage(content: string): void {
		let message: Messages = this.chatService.createMessage(content);
		let event: Events = {type: EventTypes.CHAT_MESSAGE, payload: message};
		this.chatService.emitToServer(event);
		this.addToMessages(message);
	}

	addToMessages(message: Messages): void {
		this.messages.push(message);
	}
	
}
