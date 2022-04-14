<template>
  <div class="container is-fluid">
    <div class="columns">
      <!--文章详情-->
      <div class="column is-four-fifths">
        <!--主题-->
        <el-card shadow="never">
          <div
            slot="header"
            class="has-text-centered box has-background-info-light"
          >
            <el-tooltip
              effect="dark"
              content="标题"
              placement="top-start"
              style="font-size:30px"
            >
              <el-button>{{ this.topic.title }}</el-button>
            </el-tooltip>
            <br />
            <br />
            <div class="has-text-grey is-size-7 mt-3">
              <el-divider direction="vertical" />
              <span>用户名：{{ topicUser.username }}</span>
              <el-divider direction="vertical" />
              <span>昵称：{{ topicUser.alias }}</span>
              <el-divider direction="vertical" />
              <span>浏览量：{{ this.topic.view }}</span>
            </div>
          </div>
          <!--Markdown-->
          <div id="preview" />

          <!--标签-->
          <!-- <nav class="level has-text-grey is-size-7 mt-6">
            <div class="level-left">
              <p class="level-item">
                <b-icon pack="fa" icon="tags" size="is-medium" type="is-dark">
                </b-icon>
                <b-taglist>
                  <router-link
                    v-for="(tag, index) in tags"
                    :key="index"
                    :to="{ name: 'tag', params: { name: tag.name } }"
                  >
                    <b-tag type="is-info is-light mr-1">
                      {{ "#" + tag.name }}
                    </b-tag>
                  </router-link>
                </b-taglist>
              </p>
            </div>
          </nav> -->
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchArticleDetail } from "@/api/apis/article.js";
import { fetchGetUserInfo } from "@/api/apis/user.js";
import Vditor from "vditor";
import "vditor/dist/index.css";

export default {
  name: "TopicDetail",
  data() {
    return {
      flag: false,
      topic: {
        content: "",
        id: this.$route.params.id,
        title: "",
        view: 0
      },
      tags: [],
      topicUser: {
        username: "无名",
        alias: "无名"
      }
    };
  },
  mounted() {
    this.fetchTopic();
  },
  computed: {
    compiledMarkdown() {
      return marked(this.topic.content, { sanitize: true });
    }
  },

  methods: {
    renderMarkdown(md) {
      Vditor.preview(document.getElementById("preview"), md, {
        hljs: { style: "github" }
      });
    },
    // 初始化
    async fetchTopic() {
      fetchArticleDetail(this.$route.params.id).then(response => {
        const { data } = response;
        //console.log(data);
        console.log(data.data.topic);
        this.topic.title = data.data.topic.title;
        this.topic.content = data.data.topic.content;
        this.topic.view = data.data.topic.view;
        this.renderMarkdown(this.topic.content);
        fetchGetUserInfo(data.data.topic.userId).then(response => {
          if (response.data.code === 200) {
            this.topicUser.username = response.data.data.username;
            this.topicUser.alias = response.data.data.alias;
          }
        });

        // this.tags = data.tags;
        // this.topicUser = data.user;
        // // this.comments = data.comments
        // this.renderMarkdown(this.topic.content);
        // this.flag = true;
      });
    }
  }
};
</script>

<style>
#preview {
  min-height: 300px;
}
.card {
  margin-top: 1px;
  margin-bottom: 15px;
} /*这里定义的是侧边栏的间距*/
.box {
  padding: 1px;
}
</style>
