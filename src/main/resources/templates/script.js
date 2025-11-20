import { Client } from '@stomp/stompjs';

const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onConnect: () => {
        client.subscribe('/topic', message =>
            console.log(`Received: ${message.body}`)
        );
        client.publish({ destination: '/topic', body: 'First Message' });
    },
});

client.activate();
