def parse_to_text(code, message):
    if code == 'context_length_exceeded':
        return '您的请求字数超出最大限制，请删除部分消息，或创建新的会话。'
    if code == 'content_policy_violation':
        return '你的请求因为我们的安全系统而被拒绝。你的提示可能包含了我们安全系统不允许的文本。'
    if code == 'content_filter':
        return '你的请求因为我们的安全系统而被拒绝。你的提示可能包含了我们安全系统不允许的文本。'

