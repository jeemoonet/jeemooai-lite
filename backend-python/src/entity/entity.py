import uuid


class ai_request_log:
    def __init__(self):
        self.uuid = str(uuid.uuid4())
        self.userId = None
        self.key = None
        self.wordCount = None
        self.requestTime = None
        self.model = None
        self.errorMessage = None
        self.sendMessage = None
        self.receiveMessage = None
        self.promptId = None
        self.companyId = None
        self.memberId = None
        self.question = None
        self.ip = None
        self.userAgent = None
        self.functionName = None

    def to_dict(self):
        return {
            'uuid': self.uuid,
            'user_id': self.userId,
            'key': self.key,
            'word_count': self.wordCount,
            'request_time': self.requestTime,
            'model': self.model,
            'error_message': self.errorMessage,
            'send_message': self.sendMessage,
            'receive_message': self.receiveMessage,
            'prompt_id': self.promptId,
            "company_id": self.companyId,
            "member_id": self.memberId,
            "question": self.question,
            "ip": self.ip,
            "user_agent": self.userAgent,
            "function_name": self.functionName
        }
