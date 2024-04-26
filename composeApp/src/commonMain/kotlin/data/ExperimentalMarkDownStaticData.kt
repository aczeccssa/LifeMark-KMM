package data


object ExperimentalMarkDownStaticData {
    const val WEBVIEW_USERAGENT: String =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/625.20 (KHTML, like Gecko) Version/14.3.43 Safari/625.20"

    private const val LEADING_HTML: String =
        """
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title></title>
  <style>
  @font-face {
    font-family: "阿里妈妈方圆体 VF Regular";
    src: url("//at.alicdn.com/wf/webfont/ct52fhhn1uJn/bGkWAZYglZFR.woff2") format("woff2"),
    url("//at.alicdn.com/wf/webfont/ct52fhhn1uJn/2728kdxIG7fD.woff") format("woff");
    font-display: swap;
  } 
  
  ::-webkit-scrollbar {
    width: 4px;
    height: 4px;
    background-color: transparent;
  }

  ::-webkit-scrollbar-thumb {
    width: inherit;
    height: 0px;
    background-color: #ffffff22;
    -webkit-border-radius: 9999px;
    -moz-border-radius: 9999px;
    -ms-border-radius: 9999px;
    -o-border-radius: 9999px;
    border-radius: 9999px;
  }

  :root {
    line-height: 1.5;
    font-weight: 400;
    
    color-scheme: light dark;
    color: rgba(255, 255, 255, 0.87);
    background-color: var(--panel-bg);
    
    font-family: 'Quicksand', '阿里妈妈方圆体 VF Regular', 'Jetbrains Mono', Inter, system-ui, Avenir, Helvetica, Arial, sans-serif;
    font-weight: 500;
    font-synthesis: none;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    
    --primary-color: #535bf2;
    --primary-deep-color: #434acb;
    --primary-light-color: #8187e8;
    --sky-blue-color: #528DF2;
    --lighter-purple-color: #7E52F2;
    --purple-color: #B352F2;
    --lighter-yellow-color: #F2D252;
    --orange-yellow-color: #F2BC6B;
    
    --panel-bg: #141414;
    
    --primary-outter-border-radius: 16px;
    --primary-inner-border-radius: 10px;
  }

  a {
    font-weight: 500;
    color: var(--primary-color);
    text-decoration: inherit;
  }

  a:hover { color: var(--primary-deep-color); }

  html {
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow-x: hidden;
  }

  body {
    place-items: center;
    width: 100vw;
    min-width: 320px;
    min-height: 100vh;
    font-size: 1rem;
  }

  h1 {
    font-size: 1.8em;
    line-height: 1.1;
  }

  button {
    border: 2px solid transparent;
    padding: 0.6em 1.2em;
    font-size: 1em;
    font-weight: 500;
    font-family: inherit;
    background-color: #1a1a1a;
    cursor: pointer;
    transition: border-color 0.25s;
    -webkit-border-radius: 12px;
    -moz-border-radius: 12px;
    -ms-border-radius: 12px;
    -o-border-radius: 12px;
    border-radius: 12px;
    color: inherit;
  }

  button:hover { border-color: var(--primary-color); }

  button:focus, button:focus-visible { outline: 4px auto -webkit-focus-ring-color; }

  @media (prefers-color-scheme: light) {
    :root {
      color: /* #213547 */ #333333;
      background-color: #fcfcfc;
      
      --primary-color: #383fc4;
      --primary-deep-color: #747bff;
      --panel-bg: #FAFAFA;
    }
    
    a { color: #383fc4; }
    
    a:hover { color: #747bff; }
    
    ::-webkit-scrollbar-thumb { background-color: #1a1a1a22; }
  }
</style>
  </head>

  <body>
      """


    private const val TRAILING_HTML: String =
        """
  </body>
</html>
    """


    fun xHTMLContainer(content: String): String {
        return LEADING_HTML.trimIndent() + content + TRAILING_HTML.trimIndent()
    }

    const val EXAMPLE_DOCUMENTATION: String = """
<div style="height: 24px"></div>

# LifeMark 2023 Services Impression API Docs

## Description

<table>
  <thead>
    <tr>
      <th>KEY</th>
      <th>TYPE</th>
      <th>DESCRIPTION</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>id</code></td>
      <td><code>string(&#36uuid)</code></td>
      <td>Unique identifier of the account</td>
    </tr>
    <tr>
      <td><code>createDate</code></td>
      <td><code>string(&#36time)</code></td>
      <td>Date of account creation</td>
    </tr>
    <tr>
      <td><code>sobriquet</code></td>
      <td><code>string</code></td>
      <td>Personalized sobriquet for the account</td>
    </tr>
    <tr>
      <td><code>userCredentials</code></td>
      <td><code>[Object]</code></td>
      <td>User credential object</td>
    </tr>
    <tr>
      <td style="padding-left: 20px;"><code>username</code></td>
      <td><code>string</code></td>
      <td>Account username</td>
    </tr>
    <tr>
      <td style="padding-left: 20px;"><code>email</code></td>
      <td><code>string &amp; null</code></td>
      <td>Account associated email</td>
    </tr>
    <tr>
      <td style="padding-left: 20px;"><code>password</code></td>
      <td><code>string</code></td>
      <td>Account Local Credentials</td>
    </tr>
    <tr>
      <td><code>activeStatus</code></td>
      <td><code>[Object]</code></td>
      <td>Account information object</td>
    </tr>
    <tr>
      <td style="padding-left: 20px;"><code>status</code></td>
      <td><code>string</code></td>
      <td>Account blockage information</td>
    </tr>
    <tr>
      <td><code>level</code></td>
      <td><code>[Object]</code></td>
      <td>Basic account information</td>
    </tr>
    <tr>
      <td style="padding-left: 20px;"><code>level</code></td>
      <td><code>number(&#36int)</code></td>
      <td>Account level</td>
    </tr>
    <tr>
      <td style="padding-left: 20px;"><code>impressionPoints</code></td>
      <td><code>number(&#36double)</code></td>
      <td>Account available points</td>
    </tr>
    <tr>
      <td><code>currentActivity</code></td>
      <td><code>string(&#36uuid) &amp; null</code></td>
      <td>Current sharing status</td>
    </tr>
  </tbody>
</table>

## Response JSON

### Register && Logon Response

- ***<span style="color: purple">@Post</span>&nbsp;&nbsp;<span>Registered Account&nbsp;:&nbsp;&nbsp;</span>***[```/user/register/```](http://localhost:3000/user/register)
- ***<span style="color: purple">@Post</span>&nbsp;&nbsp;<span>Login Account&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</span>***[```/user/logon/```](http://localhost:3000/user/logon)

```json
  {
      "id": "25CC3445-2ADE-49A8-8171-868292EDFA72",
      "createDate": "2023-10-10T11:04:18.000Z",
      "sobriquet": "_0001",
      "userCredentials": {
          "username": "Lester E",
          "email": null,
          "password": "eyJhbGciOi...qNZTTRBsvM"
      },
      "activeStatus": {
          "status": "isActive"
      },
      "level": {
          "level": 1,
          "impressionPoints": 20
      },
      "currentActivity": null
  }
```

- [***Register Full Json***](../available.json/RegisterAvailableAccount.json)

### Executable Response

- ***<p style="color: orange">Available Not Token Request Path:</p>***
- ***<span style="color: purple">@Get</span>&nbsp;&nbsp;<span>Get Account By ID&nbsp;&nbsp;:&nbsp;&nbsp;</span>***[```/user/id/:userIdentifier```](http://localhost:3000/user/id/25CC3445-2ADE-49A8-8171-868292EDFA72)
- ***<span style="color: purple">@Get</span>&nbsp;&nbsp;<span>Search Account&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;</span>***[```/user/search?keyword={}```](http://localhost:3000/user/search?keyword=)
- ***<span style="color: purple">@Get</span>&nbsp;&nbsp;<span>All Account List&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</span>***[```/user/list/```](http://localhost:3000/user/list)

```json
  {
      "id": "A6309DC0-3CC8-49A2-B810-393CCE195D3F",
      "sobriquet": "_0001",
      "username": "Guest",
      "level": {
          "level": 1,
          "impressionPoints": 20
      },
      "currentActivity": null
  }
```

## API Documentation
- ***SWAGGER&nbsp;&nbsp;-&nbsp;&nbsp;[User Controller](http://localhost:3000/api#/User)***
- ***FEISHU&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;[Service Information](https://d7n9vj8ces.feishu.cn/base/UNzjbJlp3aQE2Asl3pPcVQkpn5c?from=from_copylink)***
- ***NOTION&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;[Service API Documentation](https://d7n9vj8ces.feishu.cn/base/UNzjbJlp3aQE2Asl3pPcVQkpn5c?from=from_copylink)***
"""

//    private const val UnusableMarkDownTable: String = """
//| KEY                   | TYPE                            | DESCRIPTION                            |
//| --------------------- | ------------------------------- | -------------------------------------- |
//| ***id***              | ```string(${'$'}uuid)```        | Unique identifier of the account       |
//| ***createDate***      | ```string(${'$'}-time)```       | Date of account creation               |
//| ***sobriquet***       | ```string```                    | Personalized sobriquet for the account |
//| ***userCredentials*** | ```[Object]```                  | User credential object                 |
//| ├─ *username*         | ```string```                    | Account username                       |
//| ├─ *email*            | ```string & null```             | Account associated email               |
//| └─ *password*         | ```string```                    | Account Local Credentials              |
//| ***activeStatus***    | ```[Object]```                  | Account information object             |
//| └─ *status*           | ```string```                    | Account blockage information           |
//| ***level***           | ```[Object]```                  | Basic account information              |
//| ├─ *level*            | ```number(${'$'}int)```         | Account level                          |
//| └─ *impressionPoints* | ```number(${'$'}double)```      | Account available points               |
//| ***currentActivity*** | ```string(${'$'}uuid) & null``` | Current sharing status                 |
//"""
}
