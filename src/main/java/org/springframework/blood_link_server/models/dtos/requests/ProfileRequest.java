package org.springframework.blood_link_server.models.dtos.requests;

public record ProfileRequest (

        InfosRequest infosRequest,
        SignsRequest SignsRequest,
        QuestionsRequest questionsRequest
)
{}
