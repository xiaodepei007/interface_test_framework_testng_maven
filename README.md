指令：mvn clean test allure:report
生成的报告在target/site下


本框架用testNG驱动。用idea打开,右键xml/testNG.xml run
或者用上面的指令在terminal中输入
也可以直接在右边maven点击clean之后点击test运行
框架使用的大部分demo在test/java 目录下

json 格式
```json
{
  "requests":[{ --接口的列表。在解析调用这个json的时候，调用顺序从前到后，按照定义顺序.此例只列举了一个接口。文件编码建议使用utf-8
      "scheme":"https",
      "tag":"autocomplete",  --tag标签用于唯一标记一个接口
      "host":"www.zhihu.com",
      "port":"443",
      "path":"/autocomplete",
      "method":"get", --get或者post
      "post_method":"", --选择post时可用。值可以是url_encoded form_data raw
	  "headers":{
		"h1":"v1",
		"h2":"v2"
	  },
      "textBody":{ --query和url_encoded的参数由此提供
        "token":"软件测试",
        "max_matches":"66"
      },
      "mimeTextBody": [--form_data的文本数据
        {
          "key": "some_text_name",
          "mime": "text/plain;charset=utf-8",
          "value": "some_text_value"
        },
        {
          "key": "some_text_name_another",
          "mime": "text/plain;charset=utf-8",
          "value": "some_text_value_another"
        }
      ],
    "mimeFileBody":[--form_data的文件数据
        {
          "key": "some_text_name",
          "mime": "image/png",
          "fileName": "my.png",
          "content": "class:some_text_file" --content表示数据来源于文件。
        }
      ]
     "raw":{--raw的数据
        "mime": "application/json;charset=utf-8",
        "content": "class:some_text_file"
     }
    ,
      "requestCharset":"utf-8" --请求用的编码
    }
  ]
}
```