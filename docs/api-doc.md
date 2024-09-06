# XNote

[简体中文](../readme.md) | [English](../readme-en.md) | api-doc

Base URLs:

* <a href="http://localhost:8080">开发环境: http://localhost:8080</a>

# Authentication

# 笔记相关接口

## POST 增加笔记

POST /note

> Body 请求参数

```json
{
  "title": "string",
  "content": "string",
  "isPublic": "NO",
  "typeId": 1
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[NoteAddForm](#schemanoteaddform)| 否 | NoteAddForm|none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE 批量删除笔记

DELETE /note

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|noteIds|query|array[string]| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 更新笔记

PUT /note

> Body 请求参数

```json
{
  "id": 1,
  "title": "string",
  "content": "string",
  "isPublic": "NO",
  "typeId": 0
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[NoteUpdateForm](#schemanoteupdateform)| 否 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 批量更新笔记类型

PUT /note/types

> Body 请求参数

```json
{
  "noteIds": [
    0
  ],
  "typeId": 1
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[NoteUpdateTypeForm](#schemanoteupdatetypeform)| 否 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 查看笔记

GET /note/{noteId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|noteId|path|string| 是 ||none|
|shareCode|query|string| 否 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "id": 0,
    "title": "",
    "content": "",
    "userId": 0,
    "releaseTime": "",
    "updateTime": "",
    "isPublic": "",
    "typeId": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 搜索笔记

GET /note/search

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|current|query|integer| 否 ||none|
|size|query|integer| 否 ||none|
|search|query|string| 否 ||none|
|heightLightPreTag|query|string| 否 ||none|
|heightLightPostTag|query|string| 否 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "title": "",
        "content": "",
        "userId": 0,
        "releaseTime": "",
        "updateTime": "",
        "isPublic": "",
        "typeId": 0
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 搜索用户笔记

GET /note/search/{userId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userId|path|string| 是 ||none|
|current|query|integer| 否 ||none|
|size|query|integer| 否 ||none|
|search|query|string| 否 ||none|
|typeId|query|integer| 否 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "title": "",
        "userId": 0,
        "releaseTime": "",
        "isPublic": "",
        "updateTime": "",
        "typeId": 0
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 用户相关接口

## POST 注册

POST /user/register

> Body 请求参数

```json
{
  "name": "string",
  "account": "string",
  "password": "string",
  "email": "string",
  "verificationCode": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[UserRegisterForm](#schemauserregisterform)| 否 | UserRegisterForm|none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 登录

POST /user/login

> Body 请求参数

```json
{
  "account": "string",
  "password": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[UserLoginForm](#schemauserloginform)| 否 | UserLoginForm|none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 注销

POST /user/logout

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 根据id获取用户信息

GET /user/{userId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userId|path|string| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "id": 0,
    "name": "",
    "account": "",
    "email": "",
    "status": "",
    "homePageNoteId": 0,
    "imageStorageSize": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取自己的用户信息

GET /user/me

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "id": 0,
    "name": "",
    "account": "",
    "email": "",
    "status": "",
    "homePageNoteId": 0,
    "imageStorageSize": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 刷新token

GET /user/new-token

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 更新用户信息

PUT /user

> Body 请求参数

```json
{
  "name": "string",
  "homePageNoteId": 1
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[UserUpdateForm](#schemauserupdateform)| 否 | UserUpdateForm|none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 更新密码

PUT /user/password

> Body 请求参数

```json
{
  "password": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[UserUpdatePasswordForm](#schemauserupdatepasswordform)| 否 | UserUpdatePasswordForm|none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 更新邮箱

PUT /user/email

> Body 请求参数

```json
{
  "email": "string",
  "verificationCode": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[UserUpdateEmailForm](#schemauserupdateemailform)| 否 | UserUpdateEmailForm|none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 图片相关接口

## POST 上传图片

POST /image

> Body 请求参数

```yaml
uploadImageFile: string

```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» uploadImageFile|body|string(binary)| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE 删除图片

DELETE /image

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userImageIds|query|array[string]| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 预览图片

GET /image/name/{imageName}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|imageName|path|string| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 搜索自己的图片

GET /image/me

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|page|query|integer| 否 ||none|
|size|query|integer| 否 ||none|
|search|query|string| 否 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "userId": 0,
        "imageId": 0,
        "alias": "",
        "lastDownloadTime": ""
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 笔记分类相关接口

## POST 添加笔记分类

POST /note-type

> Body 请求参数

```json
{
  "name": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[NoteTypeAddForm](#schemanotetypeaddform)| 否 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE 删除笔记分类

DELETE /note-type

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|ids|query|array[string]| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 修改笔记分类

PUT /note-type

> Body 请求参数

```json
{
  "id": 1,
  "name": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[NoteTypeUpdateForm](#schemanotetypeupdateform)| 否 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取用户所有笔记分类

GET /note-type/user/{userId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userId|path|string| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": [
    {
      "id": 0,
      "name": "",
      "userId": 0
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 请求错误相关接口

## GET 请求路径不存在

GET /**

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 笔记分享记录相关接口

## POST 分享笔记

POST /share-note-record

> Body 请求参数

```json
{
  "noteId": 1,
  "expireTime": "new Timestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7)"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[ShareNoteRecordCreateForm](#schemasharenoterecordcreateform)| 否 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE 批量删除分享的记录

DELETE /share-note-record

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|shareNoteRecordIds|query|array[string]| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 修改笔记分享记录

PUT /share-note-record

> Body 请求参数

```json
{
  "id": 0,
  "expireTime": "new Timestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7)"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|[ShareNoteRecordUpdateForm](#schemasharenoterecordupdateform)| 否 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取分享的记录

GET /share-note-record/me

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|current|query|integer| 否 ||none|
|size|query|integer| 否 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "code": "",
        "noteId": 0,
        "userId": 0,
        "expireTime": "",
        "title": ""
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 验证码相关接口

## GET 发送验证码到邮箱

GET /verification-code/send/to-email

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|email|query|string| 是 ||none|
|imageCode|query|string| 是 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取图片验证码的base64

GET /verification-code/image/base64

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "status": 0,
  "message": "",
  "data": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取图片验证码

GET /verification-code/image

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 数据模型

<h2 id="tocS_Result«User»">Result«User»</h2>

<a id="schemaresult«user»"></a>
<a id="schema_Result«User»"></a>
<a id="tocSresult«user»"></a>
<a id="tocsresult«user»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "id": 0,
    "name": "string",
    "account": "string",
    "email": "string",
    "status": "DISABLE",
    "homePageNoteId": 0,
    "imageStorageSize": 0
  }
}

```

Result«User»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[User](#schemauser)|false|none||none|

<h2 id="tocS_User">User</h2>

<a id="schemauser"></a>
<a id="schema_User"></a>
<a id="tocSuser"></a>
<a id="tocsuser"></a>

```json
{
  "id": 0,
  "name": "string",
  "account": "string",
  "email": "string",
  "status": "DISABLE",
  "homePageNoteId": 0,
  "imageStorageSize": 0
}

```

User

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|name|string¦null|false|none||none|
|account|string¦null|false|none||none|
|email|string¦null|false|none||none|
|status|string¦null|false|none||none|
|homePageNoteId|integer¦null|false|none||none|
|imageStorageSize|integer¦null|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|status|DISABLE|
|status|ENABLE|

<h2 id="tocS_Note">Note</h2>

<a id="schemanote"></a>
<a id="schema_Note"></a>
<a id="tocSnote"></a>
<a id="tocsnote"></a>

```json
{
  "id": 0,
  "title": "string",
  "content": "string",
  "userId": 0,
  "releaseTime": "string",
  "updateTime": "string",
  "isPublic": "NO",
  "typeId": 0
}

```

Note

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|title|string¦null|false|none||none|
|content|string¦null|false|none||none|
|userId|integer¦null|false|none||none|
|releaseTime|string¦null|false|none||none|
|updateTime|string¦null|false|none||none|
|isPublic|string¦null|false|none||none|
|typeId|integer¦null|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|isPublic|NO|
|isPublic|YES|

<h2 id="tocS_Result«Note»">Result«Note»</h2>

<a id="schemaresult«note»"></a>
<a id="schema_Result«Note»"></a>
<a id="tocSresult«note»"></a>
<a id="tocsresult«note»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "id": 0,
    "title": "string",
    "content": "string",
    "userId": 0,
    "releaseTime": "string",
    "updateTime": "string",
    "isPublic": "NO",
    "typeId": 0
  }
}

```

Result«Note»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[Note](#schemanote)|false|none||none|

<h2 id="tocS_Image">Image</h2>

<a id="schemaimage"></a>
<a id="schema_Image"></a>
<a id="tocSimage"></a>
<a id="tocsimage"></a>

```json
{
  "id": 0,
  "userId": 0,
  "alias": "string",
  "name": "string",
  "uploadTime": "string",
  "url": "string",
  "size": 0
}

```

Image

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|userId|integer¦null|false|none||none|
|alias|string¦null|false|none||none|
|name|string¦null|false|none||none|
|uploadTime|string¦null|false|none||none|
|url|string¦null|false|none||none|
|size|integer¦null|false|none||none|

<h2 id="tocS_NoteType">NoteType</h2>

<a id="schemanotetype"></a>
<a id="schema_NoteType"></a>
<a id="tocSnotetype"></a>
<a id="tocsnotetype"></a>

```json
{
  "id": 0,
  "name": "string",
  "userId": 0
}

```

NoteType

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|name|string¦null|false|none||none|
|userId|integer¦null|false|none||none|

<h2 id="tocS_Result«List«NoteType»»">Result«List«NoteType»»</h2>

<a id="schemaresult«list«notetype»»"></a>
<a id="schema_Result«List«NoteType»»"></a>
<a id="tocSresult«list«notetype»»"></a>
<a id="tocsresult«list«notetype»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": [
    {
      "id": 0,
      "name": "string",
      "userId": 0
    }
  ]
}

```

Result«List«NoteType»»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[[NoteType](#schemanotetype)]¦null|false|none||none|

<h2 id="tocS_NoteAddForm">NoteAddForm</h2>

<a id="schemanoteaddform"></a>
<a id="schema_NoteAddForm"></a>
<a id="tocSnoteaddform"></a>
<a id="tocsnoteaddform"></a>

```json
{
  "title": "string",
  "content": "string",
  "isPublic": "NO",
  "typeId": 1
}

```

NoteAddForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|title|string¦null|true|none||none|
|content|string¦null|true|none||none|
|isPublic|string¦null|true|none||none|
|typeId|integer¦null|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|isPublic|NO|
|isPublic|YES|

<h2 id="tocS_NoteShareForm">NoteShareForm</h2>

<a id="schemanoteshareform"></a>
<a id="schema_NoteShareForm"></a>
<a id="tocSnoteshareform"></a>
<a id="tocsnoteshareform"></a>

```json
{
  "noteId": 1,
  "expireTime": "new Date(System.currentTimeMillis() + 1000 * 3600 * 24 * 7)"
}

```

NoteShareForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|noteId|integer¦null|true|none||none|
|expireTime|string¦null|false|none||默认为7天后过期|

<h2 id="tocS_PageShareNoteVO">PageShareNoteVO</h2>

<a id="schemapagesharenotevo"></a>
<a id="schema_PageShareNoteVO"></a>
<a id="tocSpagesharenotevo"></a>
<a id="tocspagesharenotevo"></a>

```json
{
  "id": 0,
  "code": "string",
  "noteId": 0,
  "userId": 0,
  "expireTime": "string",
  "title": "string"
}

```

PageShareNoteVO

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|code|string¦null|false|none||none|
|noteId|integer¦null|false|none||none|
|userId|integer¦null|false|none||none|
|expireTime|string¦null|false|none||none|
|title|string¦null|false|none||none|

<h2 id="tocS_PageVO«Image»">PageVO«Image»</h2>

<a id="schemapagevo«image»"></a>
<a id="schema_PageVO«Image»"></a>
<a id="tocSpagevo«image»"></a>
<a id="tocspagevo«image»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "userId": 0,
      "alias": "string",
      "name": "string",
      "uploadTime": "string",
      "url": "string",
      "size": 0
    }
  ]
}

```

PageVO«Image»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[Image](#schemaimage)]¦null|false|none||none|

<h2 id="tocS_PageVO«PageShareNoteVO»">PageVO«PageShareNoteVO»</h2>

<a id="schemapagevo«pagesharenotevo»"></a>
<a id="schema_PageVO«PageShareNoteVO»"></a>
<a id="tocSpagevo«pagesharenotevo»"></a>
<a id="tocspagevo«pagesharenotevo»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "code": "string",
      "noteId": 0,
      "userId": 0,
      "expireTime": "string",
      "title": "string"
    }
  ]
}

```

PageVO«PageShareNoteVO»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[PageShareNoteVO](#schemapagesharenotevo)]¦null|false|none||none|

<h2 id="tocS_Result«PageVO«Image»»">Result«PageVO«Image»»</h2>

<a id="schemaresult«pagevo«image»»"></a>
<a id="schema_Result«PageVO«Image»»"></a>
<a id="tocSresult«pagevo«image»»"></a>
<a id="tocsresult«pagevo«image»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "userId": 0,
        "alias": "string",
        "name": "string",
        "uploadTime": "string",
        "url": "string",
        "size": 0
      }
    ]
  }
}

```

Result«PageVO«Image»»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«Image»](#schemapagevo%c2%abimage%c2%bb)|false|none||none|

<h2 id="tocS_Result«PageVO«PageShareNoteVO»»">Result«PageVO«PageShareNoteVO»»</h2>

<a id="schemaresult«pagevo«pagesharenotevo»»"></a>
<a id="schema_Result«PageVO«PageShareNoteVO»»"></a>
<a id="tocSresult«pagevo«pagesharenotevo»»"></a>
<a id="tocsresult«pagevo«pagesharenotevo»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "code": "string",
        "noteId": 0,
        "userId": 0,
        "expireTime": "string",
        "title": "string"
      }
    ]
  }
}

```

Result«PageVO«PageShareNoteVO»»

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«PageShareNoteVO»](#schemapagevo%c2%abpagesharenotevo%c2%bb)|false|none||none|

<h2 id="tocS_UserLoginForm">UserLoginForm</h2>

<a id="schemauserloginform"></a>
<a id="schema_UserLoginForm"></a>
<a id="tocSuserloginform"></a>
<a id="tocsuserloginform"></a>

```json
{
  "account": "string",
  "password": "string"
}

```

UserLoginForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|account|string¦null|true|none||none|
|password|string¦null|true|none||none|

<h2 id="tocS_UserRegisterForm">UserRegisterForm</h2>

<a id="schemauserregisterform"></a>
<a id="schema_UserRegisterForm"></a>
<a id="tocSuserregisterform"></a>
<a id="tocsuserregisterform"></a>

```json
{
  "name": "string",
  "account": "string",
  "password": "string",
  "email": "string",
  "verificationCode": "string"
}

```

UserRegisterForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|name|string¦null|true|none||none|
|account|string¦null|true|none||none|
|password|string¦null|true|none||none|
|email|string¦null|true|none||none|
|verificationCode|string¦null|true|none||none|

<h2 id="tocS_UserUpdateEmailForm">UserUpdateEmailForm</h2>

<a id="schemauserupdateemailform"></a>
<a id="schema_UserUpdateEmailForm"></a>
<a id="tocSuserupdateemailform"></a>
<a id="tocsuserupdateemailform"></a>

```json
{
  "email": "string",
  "verificationCode": "string"
}

```

UserUpdateEmailForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|email|string¦null|true|none||none|
|verificationCode|string¦null|true|none||none|

<h2 id="tocS_UserUpdateForm">UserUpdateForm</h2>

<a id="schemauserupdateform"></a>
<a id="schema_UserUpdateForm"></a>
<a id="tocSuserupdateform"></a>
<a id="tocsuserupdateform"></a>

```json
{
  "name": "string",
  "homePageNoteId": 1
}

```

UserUpdateForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|name|string¦null|false|none||none|
|homePageNoteId|integer¦null|false|none||none|

<h2 id="tocS_UserUpdatePasswordForm">UserUpdatePasswordForm</h2>

<a id="schemauserupdatepasswordform"></a>
<a id="schema_UserUpdatePasswordForm"></a>
<a id="tocSuserupdatepasswordform"></a>
<a id="tocsuserupdatepasswordform"></a>

```json
{
  "password": "string"
}

```

UserUpdatePasswordForm

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|password|string¦null|false|none||none|

<h2 id="tocS_Result«String»">Result«String»</h2>

<a id="schemaresult«string»"></a>
<a id="schema_Result«String»"></a>
<a id="tocSresult«string»"></a>
<a id="tocsresult«string»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|string¦null|false|none||none|

<h2 id="tocS_Result«Object»">Result«Object»</h2>

<a id="schemaresult«object»"></a>
<a id="schema_Result«Object»"></a>
<a id="tocSresult«object»"></a>
<a id="tocsresult«object»"></a>

```json
{
  "status": null,
  "message": null,
  "data": null
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|null¦null|false|none||none|
|message|null¦null|false|none||none|
|data|null¦null|false|none||none|

<h2 id="tocS_Result«Integer»">Result«Integer»</h2>

<a id="schemaresult«integer»"></a>
<a id="schema_Result«Integer»"></a>
<a id="tocSresult«integer»"></a>
<a id="tocsresult«integer»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|integer¦null|false|none||none|

<h2 id="tocS_ShareNoteVO">ShareNoteVO</h2>

<a id="schemasharenotevo"></a>
<a id="schema_ShareNoteVO"></a>
<a id="tocSsharenotevo"></a>
<a id="tocssharenotevo"></a>

```json
{
  "id": 0,
  "title": "string",
  "userId": 0,
  "releaseTime": "string",
  "isPublic": "NO",
  "updateTime": "string",
  "typeId": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|title|string¦null|false|none||none|
|userId|integer¦null|false|none||none|
|releaseTime|string¦null|false|none||none|
|isPublic|string¦null|false|none||none|
|updateTime|string¦null|false|none||none|
|typeId|integer¦null|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|isPublic|NO|
|isPublic|YES|

<h2 id="tocS_PageVO«ShareNoteVO»">PageVO«ShareNoteVO»</h2>

<a id="schemapagevo«sharenotevo»"></a>
<a id="schema_PageVO«ShareNoteVO»"></a>
<a id="tocSpagevo«sharenotevo»"></a>
<a id="tocspagevo«sharenotevo»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "title": "string",
      "userId": 0,
      "releaseTime": "string",
      "isPublic": "NO",
      "updateTime": "string",
      "typeId": 0
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[ShareNoteVO](#schemasharenotevo)]¦null|false|none||none|

<h2 id="tocS_Result«PageVO«ShareNoteVO»»">Result«PageVO«ShareNoteVO»»</h2>

<a id="schemaresult«pagevo«sharenotevo»»"></a>
<a id="schema_Result«PageVO«ShareNoteVO»»"></a>
<a id="tocSresult«pagevo«sharenotevo»»"></a>
<a id="tocsresult«pagevo«sharenotevo»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "title": "string",
        "userId": 0,
        "releaseTime": "string",
        "isPublic": "NO",
        "updateTime": "string",
        "typeId": 0
      }
    ]
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«ShareNoteVO»](#schemapagevo%c2%absharenotevo%c2%bb)|false|none||none|

<h2 id="tocS_NoteUpdateForm">NoteUpdateForm</h2>

<a id="schemanoteupdateform"></a>
<a id="schema_NoteUpdateForm"></a>
<a id="tocSnoteupdateform"></a>
<a id="tocsnoteupdateform"></a>

```json
{
  "id": 1,
  "title": "string",
  "content": "string",
  "isPublic": "NO",
  "typeId": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|true|none||none|
|title|string¦null|false|none||none|
|content|string¦null|false|none||none|
|isPublic|string¦null|false|none||none|
|typeId|integer¦null|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|isPublic|NO|
|isPublic|YES|

<h2 id="tocS_Result«?»">Result«?»</h2>

<a id="schemaresult«?»"></a>
<a id="schema_Result«?»"></a>
<a id="tocSresult«?»"></a>
<a id="tocsresult«?»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": null
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|null¦null|false|none||none|

<h2 id="tocS_SearchNoteVO">SearchNoteVO</h2>

<a id="schemasearchnotevo"></a>
<a id="schema_SearchNoteVO"></a>
<a id="tocSsearchnotevo"></a>
<a id="tocssearchnotevo"></a>

```json
{
  "id": 0,
  "title": "string",
  "userId": 0,
  "releaseTime": "string",
  "isPublic": "NO",
  "updateTime": "string",
  "typeId": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|title|string¦null|false|none||none|
|userId|integer¦null|false|none||none|
|releaseTime|string¦null|false|none||none|
|isPublic|string¦null|false|none||none|
|updateTime|string¦null|false|none||none|
|typeId|integer¦null|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|isPublic|NO|
|isPublic|YES|

<h2 id="tocS_PageVO«SearchNoteVO»">PageVO«SearchNoteVO»</h2>

<a id="schemapagevo«searchnotevo»"></a>
<a id="schema_PageVO«SearchNoteVO»"></a>
<a id="tocSpagevo«searchnotevo»"></a>
<a id="tocspagevo«searchnotevo»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "title": "string",
      "userId": 0,
      "releaseTime": "string",
      "isPublic": "NO",
      "updateTime": "string",
      "typeId": 0
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[SearchNoteVO](#schemasearchnotevo)]¦null|false|none||none|

<h2 id="tocS_Result«PageVO«SearchNoteVO»»">Result«PageVO«SearchNoteVO»»</h2>

<a id="schemaresult«pagevo«searchnotevo»»"></a>
<a id="schema_Result«PageVO«SearchNoteVO»»"></a>
<a id="tocSresult«pagevo«searchnotevo»»"></a>
<a id="tocsresult«pagevo«searchnotevo»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "title": "string",
        "userId": 0,
        "releaseTime": "string",
        "isPublic": "NO",
        "updateTime": "string",
        "typeId": 0
      }
    ]
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«SearchNoteVO»](#schemapagevo%c2%absearchnotevo%c2%bb)|false|none||none|

<h2 id="tocS_NoteUpdateTypeForm">NoteUpdateTypeForm</h2>

<a id="schemanoteupdatetypeform"></a>
<a id="schema_NoteUpdateTypeForm"></a>
<a id="tocSnoteupdatetypeform"></a>
<a id="tocsnoteupdatetypeform"></a>

```json
{
  "noteIds": [
    0
  ],
  "typeId": 1
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|noteIds|[integer]¦null|true|none||none|
|typeId|integer¦null|true|none||none|

<h2 id="tocS_Result«Set«String»»">Result«Set«String»»</h2>

<a id="schemaresult«set«string»»"></a>
<a id="schema_Result«Set«String»»"></a>
<a id="tocSresult«set«string»»"></a>
<a id="tocsresult«set«string»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": [
    "string"
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[string]¦null|false|none||none|

<h2 id="tocS_ShareNoteRecordCreateForm">ShareNoteRecordCreateForm</h2>

<a id="schemasharenoterecordcreateform"></a>
<a id="schema_ShareNoteRecordCreateForm"></a>
<a id="tocSsharenoterecordcreateform"></a>
<a id="tocssharenoterecordcreateform"></a>

```json
{
  "noteId": 1,
  "expireTime": "new Timestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7)"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|noteId|integer¦null|true|none||none|
|expireTime|string¦null|false|none||默认为7天后过期|

<h2 id="tocS_ShareNoteRecordVO">ShareNoteRecordVO</h2>

<a id="schemasharenoterecordvo"></a>
<a id="schema_ShareNoteRecordVO"></a>
<a id="tocSsharenoterecordvo"></a>
<a id="tocssharenoterecordvo"></a>

```json
{
  "id": 0,
  "code": "string",
  "noteId": 0,
  "userId": 0,
  "expireTime": "string",
  "title": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|code|string¦null|false|none||none|
|noteId|integer¦null|false|none||none|
|userId|integer¦null|false|none||none|
|expireTime|string¦null|false|none||none|
|title|string¦null|false|none||none|

<h2 id="tocS_PageVO«ShareNoteRecordVO»">PageVO«ShareNoteRecordVO»</h2>

<a id="schemapagevo«sharenoterecordvo»"></a>
<a id="schema_PageVO«ShareNoteRecordVO»"></a>
<a id="tocSpagevo«sharenoterecordvo»"></a>
<a id="tocspagevo«sharenoterecordvo»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "code": "string",
      "noteId": 0,
      "userId": 0,
      "expireTime": "string",
      "title": "string"
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[ShareNoteRecordVO](#schemasharenoterecordvo)]¦null|false|none||none|

<h2 id="tocS_Result«PageVO«ShareNoteRecordVO»»">Result«PageVO«ShareNoteRecordVO»»</h2>

<a id="schemaresult«pagevo«sharenoterecordvo»»"></a>
<a id="schema_Result«PageVO«ShareNoteRecordVO»»"></a>
<a id="tocSresult«pagevo«sharenoterecordvo»»"></a>
<a id="tocsresult«pagevo«sharenoterecordvo»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "code": "string",
        "noteId": 0,
        "userId": 0,
        "expireTime": "string",
        "title": "string"
      }
    ]
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«ShareNoteRecordVO»](#schemapagevo%c2%absharenoterecordvo%c2%bb)|false|none||none|

<h2 id="tocS_ShareNoteRecordUpdateForm">ShareNoteRecordUpdateForm</h2>

<a id="schemasharenoterecordupdateform"></a>
<a id="schema_ShareNoteRecordUpdateForm"></a>
<a id="tocSsharenoterecordupdateform"></a>
<a id="tocssharenoterecordupdateform"></a>

```json
{
  "id": 0,
  "expireTime": "new Timestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7)"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|expireTime|string¦null|false|none||none|

<h2 id="tocS_SearchUserImageVO">SearchUserImageVO</h2>

<a id="schemasearchuserimagevo"></a>
<a id="schema_SearchUserImageVO"></a>
<a id="tocSsearchuserimagevo"></a>
<a id="tocssearchuserimagevo"></a>

```json
{
  "id": 0,
  "userId": 0,
  "imageId": 0,
  "alias": "string",
  "lastDownloadTime": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|false|none||none|
|userId|integer¦null|false|none||none|
|imageId|integer¦null|false|none||none|
|alias|string¦null|false|none||none|
|lastDownloadTime|string¦null|false|none||none|

<h2 id="tocS_PageVO«SearchUserImageVO»">PageVO«SearchUserImageVO»</h2>

<a id="schemapagevo«searchuserimagevo»"></a>
<a id="schema_PageVO«SearchUserImageVO»"></a>
<a id="tocSpagevo«searchuserimagevo»"></a>
<a id="tocspagevo«searchuserimagevo»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "userId": 0,
      "imageId": 0,
      "alias": "string",
      "lastDownloadTime": "string"
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[SearchUserImageVO](#schemasearchuserimagevo)]¦null|false|none||none|

<h2 id="tocS_Result«PageVO«SearchUserImageVO»»">Result«PageVO«SearchUserImageVO»»</h2>

<a id="schemaresult«pagevo«searchuserimagevo»»"></a>
<a id="schema_Result«PageVO«SearchUserImageVO»»"></a>
<a id="tocSresult«pagevo«searchuserimagevo»»"></a>
<a id="tocsresult«pagevo«searchuserimagevo»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "userId": 0,
        "imageId": 0,
        "alias": "string",
        "lastDownloadTime": "string"
      }
    ]
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«SearchUserImageVO»](#schemapagevo%c2%absearchuserimagevo%c2%bb)|false|none||none|

<h2 id="tocS_PageVO«Note»">PageVO«Note»</h2>

<a id="schemapagevo«note»"></a>
<a id="schema_PageVO«Note»"></a>
<a id="tocSpagevo«note»"></a>
<a id="tocspagevo«note»"></a>

```json
{
  "current": 0,
  "page": 0,
  "size": 0,
  "total": 0,
  "records": [
    {
      "id": 0,
      "title": "string",
      "content": "string",
      "userId": 0,
      "releaseTime": "string",
      "updateTime": "string",
      "isPublic": "NO",
      "typeId": 0
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|current|integer¦null|false|none||none|
|page|integer¦null|false|none||none|
|size|integer¦null|false|none||none|
|total|integer¦null|false|none||none|
|records|[[Note](#schemanote)]¦null|false|none||none|

<h2 id="tocS_Result«PageVO«Note»»">Result«PageVO«Note»»</h2>

<a id="schemaresult«pagevo«note»»"></a>
<a id="schema_Result«PageVO«Note»»"></a>
<a id="tocSresult«pagevo«note»»"></a>
<a id="tocsresult«pagevo«note»»"></a>

```json
{
  "status": 0,
  "message": "string",
  "data": {
    "current": 0,
    "page": 0,
    "size": 0,
    "total": 0,
    "records": [
      {
        "id": 0,
        "title": "string",
        "content": "string",
        "userId": 0,
        "releaseTime": "string",
        "updateTime": "string",
        "isPublic": "NO",
        "typeId": 0
      }
    ]
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|status|integer¦null|false|none||none|
|message|string¦null|false|none||none|
|data|[PageVO«Note»](#schemapagevo%c2%abnote%c2%bb)|false|none||none|

<h2 id="tocS_NoteTypeAddForm">NoteTypeAddForm</h2>

<a id="schemanotetypeaddform"></a>
<a id="schema_NoteTypeAddForm"></a>
<a id="tocSnotetypeaddform"></a>
<a id="tocsnotetypeaddform"></a>

```json
{
  "name": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|name|string¦null|true|none||none|

<h2 id="tocS_NoteTypeUpdateForm">NoteTypeUpdateForm</h2>

<a id="schemanotetypeupdateform"></a>
<a id="schema_NoteTypeUpdateForm"></a>
<a id="tocSnotetypeupdateform"></a>
<a id="tocsnotetypeupdateform"></a>

```json
{
  "id": 1,
  "name": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer¦null|true|none||none|
|name|string¦null|true|none||none|

