package com.sanisamoj.database.mongodb

enum class Fields(val title: String) {
    Id(title = "_id"),
    Email(title = "email"),
    Name(title = "name"),
    Phone(title = "phone"),
    Status(title = "status"),
    ApplicationName(title = "applicationName"),
    Number(title = "number"),
    ApplicationId(title = "applicationId"),
    Password(title = "password"),
    EventType(title = "eventType"),
    Severity(title = "severity"),
    ValidationCode(title = "validationCode"),
    Read(title = "read"),
    Timestamp(title = "timestamp")
}