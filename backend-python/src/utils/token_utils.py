import jwt
import os
import base64

token_key = os.getenv('TOKEN_KEY', '9kgp+ik6FssSeboiH8jbcfdEYegdqTuIOXheBpiJKGU=')


def hasAuth(authorization):
    if not authorization:
        return None
    try:
        if authorization.startswith("Bearer "):
            authorization = authorization[7:]
        return jwt.decode(authorization, token_key, algorithms=["HS256"])
    except jwt.ExpiredSignatureError:
        print("JWT token has expired")
    except jwt.InvalidTokenError:
        print("Invalid token")