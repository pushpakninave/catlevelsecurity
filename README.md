# catlevelsecurity
`curl -i -u 123:123 http://localhost:8083/` : using this curl command to hit an endpoint using username and password.

### Q. CSRF(cross site request forgery)

- when you login via uname and password. The server accordingly returns the session id associated with the user.

### Q. when session id can be bad?

- lets say you download a movie for free from a website. Before login, the attacker can set or predict the session ID and can force the victim to use it. Once authenticated the guy can hack and get access to the session. This is also known as session hijacking attack, man in the middle(MITM), Cross site scripting(XSS).

- while downloading this site might run a script to get the session id and using that valid session id it might request server and get the vulnerable information.

### Q. Then wtf are we even using CSRF for?

- To secure the user's active session.

1. Use CSRF tokens for state changing or edit requests like (POST, PUT, DELETE).
2. Bind CSRF token to user session.
3. Require CSRF token in requests and validate them server side.

### Q. That is okay, But how are these steps even securing the session?

- #### CSRF Token Mitigation

    A unique token is generated for every user session, ensuring that only requests containing the correct token are executed.
    The token is included as a hidden field in forms or as a header in AJAX requests.
    Since the attacker cannot access the token (it’s tied to the user's session and not exposed via the frontend), they cannot forge a valid request.

### Q. So what is the solution provided by spring security module?

- Since HTTP is by-default stateless that means the server don't know who the user is and can be recognised only with some credentials out of which one is session id.
But since we can't generate session id every time so the spring security every time generates the CSRF token every time
if you wanna a see the csrf token just try logging out and before logging out try viewing pagesource there you can see below input section which is by default hidden.

``` html
<input name="_csrf" type="hidden" value="Zkv5YVcmu1Tnuqtv-beMH9qfX2i-WYPNRyGUvutmnOPo9mBGVXiaB2BD2mPK35sMmJq4J-KtcgmHP-Hgf0Sm2I0HrdLYl1Jx" />
```

lets try it another way

1. just make a request to any update method like put, post or delete mapping
ex. `curl -i -u 123:123 -X POST -H "Content-Type: application/json" -d '{"id":97,"name":"kohn","marks":80}' http://localhost:8083/students`
the result you see is response with 401 that is unauthorized. But Why?
2. This is because spring checks for the csrf token for update requests.
3. In this project try making `curl -v -c cookies.txt -u 123:123 http://localhost:8083/csrf` request and get the `CSRF` token value.
4. Then try making a post request

``` curl
curl -b cookies.txt -i -u 123:123 \
-X POST \
-H "Content-Type: application/json" \
-H "X-CSRF-TOKEN: -mt61qtdwb4eG-CdvA_uZT_lONn8QP59ob2XzcficHZjF0Dmzl8Z489lpYYzI9T52CLaVFyEFeCeec9Ql9mj9fLVEkFQJHjU" \
--data-raw '{"id":64,"name":"Mikey","marks":90}' \
http://localhost:8083/students
```

you will get a response like

``` curl
HTTP/1.1 200
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 15 Dec 2024 08:01:10 GMT

{"id":64,"name":"Mikey","marks":90}
```

**Since we are done with knowledge based authentication we move forward with JWT not to advance auth or secrecy but to accountability**

## What and why JWT? [here](/doc.md)

1. JSON web token.
2. Completely Stateless(i.e. it don't know who you are?).
3. 3 Parts-> [Header, Payload, Signature].
4. Signature encryption can be symmetric or asymmetric.
5. Symmetrical use common key to create JWT and validate.
6. Asymmetric use private key to create and public key to validate.

**Header**: contains encryption algorithm and type of token.

**Payload**: contains data.

**Signature**: contains encrypted data in the form of signature using the algorithm mentioned in the header.

### Integrating JWT in spring security

`Dependencies :`

```xml
<dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt-api</artifactId>
   <version>0.12.6</version>
  </dependency>
  <dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt-impl</artifactId>
   <version>0.12.6</version>
   <scope>runtime</scope>
  </dependency>
  <dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt-jackson</artifactId>
   <version>0.12.6</version>
   <scope>runtime</scope>
  </dependency>
```
