<template>
  <div class="container is-fluid">
    <div class="columns">
      <div class="column is-full">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span><i class="fa fa fa-book"> 发布文章</i></span>
          </div>
          <div>
            <el-form
              ref="ruleForm"
              :model="ruleForm"
              :rules="rules"
              class="demo-ruleForm"
            >
              <el-form-item label="标题" prop="title">
                <el-input
                  style="width:400px"
                  v-model="ruleForm.title"
                  placeholder="输入文章标题"
                />
              </el-form-item>

              <el-form-item label="作者">
                <el-input
                  style="width:200px"
                  class="inline-input"
                  v-model="username"
                  placeholder="请输入作者用户名"
                ></el-input>
              </el-form-item>

              <!--Markdown-->
              <div id="vditor" />

              <b-taginput
                v-model="ruleForm.tags"
                class="my-3"
                maxlength="15"
                maxtags="3"
                ellipsis
                placeholder="请输入主题标签，限制为 15 个字符和 3 个标签"
              />

              <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')"
                  >立即创建
                </el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchAddArticle } from "@/api/apis/article.js";
import Vditor from "vditor";
import "vditor/dist/index.css";

export default {
  name: "TopicPost",

  data() {
    return {
      contentEditor: {},
      ruleForm: {
        title: "", // 标题
        tags: [], // 标签
        content: "" // 内容
      },
      username: "",
      rules: {
        title: [
          { required: true, message: "请输入话题名称", trigger: "blur" },
          {
            min: 1,
            max: 25,
            message: "长度在 1 到 25 个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  mounted() {
    this.contentEditor = new Vditor("vditor", {
      height: 700,
      placeholder: "此处为话题内容……",
      theme: "classic",
      counter: {
        enable: true,
        type: "markdown"
      },
      preview: {
        delay: 0,
        hljs: {
          style: "monokai",
          lineNumber: true
        }
      },
      tab: "\t",
      typewriterMode: true,
      toolbarConfig: {
        pin: true
      },
      fullscreen: {
        index: 999
      },
      outline: {
        enable: true
      },
      cache: {
        enable: false
      },
      mode: "sv",
      toolbar: [
        "emoji",
        "headings",
        "bold",
        "italic",
        "strike",
        "link",
        "|",
        "list",
        "ordered-list",
        "check",
        "outdent",
        "indent",
        "|",
        "quote",
        "line",
        "code",
        "inline-code",
        "insert-before",
        "insert-after",
        "|",
        "upload",
        "record",
        "table",
        "|",
        "undo",
        "redo",
        "|",
        "edit-mode",
        "fullscreen",
        {
          name: "more",
          toolbar: [
            "both",
            "code-theme",
            "content-theme",
            "export",
            "outline",
            "preview",
            "devtools",
            "info",
            "help"
          ]
        }
      ],
      upload: {
        //允许的图片后缀
        max: 10 * 1024 * 1024,
        multiple: false,
        // 上传图片要用的url
        url: "",
        extraData: { access_token: "123" }, //为 FormData 添加额外的参数
        linkToImgUrl: "",
        // 	文件名安全处理
        filename(name) {
          return name
            .replace(/[^(a-zA-Z0-9\u4e00-\u9fa5\.)]/g, "")
            .replace(/[\?\\/:|<>\*\[\]\(\)\$%\{\}@~]/g, "")
            .replace("/\\s/g", "");
        },

        //上传成功时执行
        success(editor, msg) {
          let responseData = JSON.parse(msg);
          console.log(responseData);
          let imageUrl = responseData.url;
          let succFileText = "";
          if (vditor && vditor.vditor.currentMode === "wysiwyg") {
            succFileText += `\n <img alt=${imageUrl} src="${imageUrl}">`;
          } else {
            succFileText += `\n![${imageUrl}](${imageUrl})`;
          }
          //将图片路径写入文本
          document.execCommand("insertHTML", false, succFileText);
          alert("成功");
        },
        error() {
          alert("失败");
        }
      }
    });
  },
  methods: {
    /**
     * 提交表单
     */
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (
            this.contentEditor.getValue().length === 1 ||
            this.contentEditor.getValue() == null ||
            this.contentEditor.getValue() === ""
          ) {
            alert("内容不可为空");
            return false;
          }
          if (this.ruleForm.tags == null || this.ruleForm.tags.length === 0) {
            alert("标签不可以为空");
            return false;
          }
          this.ruleForm.content = this.contentEditor.getValue();
          fetchAddArticle(this.ruleForm, this.username).then(response => {
            if (response.data.code == 200) {
              this.$router.push("/article/" + response.data.data.id);
              this.$message({
                type: "success",
                showClose: true,
                message: "发布成功"
              });
            } else if (response.data.code == -1) {
              this.$message({
                type: "error",
                showClose: true,
                message: data.message
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    /**
     * 重置
     */
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.contentEditor.setValue("");
      this.ruleForm.tags = "";
    }
  }
};
</script>

<style></style>
