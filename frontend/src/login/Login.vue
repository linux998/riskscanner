<template>
  <div class="container" v-loading="result.loading" v-if="ready">
    <el-row type="flex">

      <el-col :span="12">
        <el-form :model="form" :rules="rules" ref="form">
          <div class="title">
            <img src="../assets/img/logo/logo-dark.png" style="width: 224px" alt="">
          </div>
          <div class="border"></div>
          <div class="welcome">
            {{$t('commons.welcome')}}
          </div>
          <div class="form">
            <el-form-item v-slot:default>
              <el-radio-group v-model="form.authenticate">
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="username">
              <el-input v-model="form.username" :placeholder="$t('commons.login_username')" autofocus
                        autocomplete="off"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="form.password" :placeholder="$t('commons.password')" show-password autocomplete="off"
                        maxlength="20" show-word-limit/>
            </el-form-item>
          </div>
          <div class="btn">
            <el-button type="primary" class="submit" @click="submit('form')">
              {{$t('commons.login')}}
            </el-button>
          </div>
          <div class="msg">
            {{msg}}
          </div>
        </el-form>
      </el-col>

      <div class="divider"/>

      <el-col :span="12" class="image">
        <div></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {saveLocalStorage} from '../common/js/utils';
import {DEFAULT_LANGUAGE} from "../common/js/constants";

/* eslint-disable */
  export default {
    name: "Login",
    data() {
      return {
        result: {},
        form: {
          username: '',
          password: '',
          authenticate: 'LOCAL'
        },
        rules: {
          username: [
            {required: true, message: this.$t('commons.input_login_username'), trigger: 'blur'},
          ],
          password: [
            {required: true, message: this.$t('commons.input_password'), trigger: 'blur'},
            {min: 6, max: 20, message: this.$t('commons.input_limit', [6, 20]), trigger: 'blur'}
          ]
        },
        msg: '',
        ready: false,
        loginUrl: 'signin',
      }
    },
    beforeCreate() {
      this.$get("/isLogin").then(response => {
        if (!response.data.success) {
          if (response.data.message === 'sso') {
            window.location.href = "/sso/login"
          } else {
            this.ready = true;
          }
        } else {
          let user = response.data.data;
          saveLocalStorage(response.data);
          this.getLanguage(user.language);
          window.location.href = "/"
        }
      });
    },
    created: function () {
      // 主页添加键盘事件,注意,不能直接在焦点事件上添加回车
      document.addEventListener("keydown", this.watchEnter);
    },
    destroyed() {
      //移除监听回车按键
      document.removeEventListener("keydown", this.watchEnter);
    },
    methods: {
      //监听回车按钮事件
      watchEnter(e) {
        let keyNum = e.which; //获取被按下的键值
        //判断如果用户按下了回车键（keycody=13）
        if (keyNum === 13) {
          // 按下回车按钮要做的事
          this.submit('form');
        }
      },
      submit(form) {
        this.$refs[form].validate((valid) => {
          if (valid) {
            switch (this.form.authenticate) {
              case "LOCAL":
                this.loginUrl = "/signin";
                this.doLogin();
                break;
              default:
                this.loginUrl = "/sso/signin";
                this.doLogin();
            }
          } else {
            return false;
          }
        });
      },
      doLogin() {
        this.result = this.$post(this.loginUrl, this.form, response => {
          saveLocalStorage(response);
          sessionStorage.setItem('loginSuccess', 'true');
          this.getLanguage(response.data.language);
        });
      },
      getLanguage(language) {
        if (!language) {
          this.$get("language", response => {
            language = response.data;
            localStorage.setItem(DEFAULT_LANGUAGE, language);
            window.location.href = "/"
          })
        } else {
          window.location.href = "/"
        }
      },
    }
  }
</script>

<style scoped>
  .container {
    width: 80%;
    min-width: 800px;
    max-width: 1440px;
    height: 560px;
    margin: calc((100vh - 560px) / 2) auto 0;
    background-color: #fff;
  }

  .logo {
    margin: 30px 30px 0;
  }

  .title {
    margin-top: 50px;
    font-size: 32px;
    letter-spacing: 0;
    text-align: center;
  }

  .title > #s1 {
    color: #999999;
  }

  .title > #s2 {
    color: #151515;
  }

  .border {
    height: 2px;
    margin: 20px auto 20px;
    position: relative;
    width: 80px;
    background: #004a71;
  }

  .welcome {
    margin-top: 50px;
    font-size: 14px;
    color: #999999;
    letter-spacing: 0;
    line-height: 18px;
    text-align: center;
  }

  .form {
    margin-top: 30px;
    padding: 0 40px;
  }

  .btn {
    margin-top: 40px;
    padding: 0 40px;
  }

  .btn > .submit {
    width: 100%;
    border-radius: 0;
    border-color: #004a71;
    background-color: #004a71;
  }

  .btn > .submit:hover {
    border-color: #003b5a;
    background-color: #003b5a;
  }

  .btn > .submit:active {
    border-color: #003b5a;
    background-color: #003b5a;
  }

  .msg {
    margin-top: 10px;
    padding: 0 40px;
    color: red;
    text-align: center;
  }

  .image {
    background: url(../assets/info.jpg);
    height: 560px;
  }

  .divider {
    border: 1px solid #f6f3f8;
    height: 480px;
    margin: 165px 0;
  }
</style>

<style>
  body {
    font-family: -apple-system, BlinkMacSystemFont, "Neue Haas Grotesk Text Pro", "Arial Nova", "Segoe UI", "Helvetica Neue", ".PingFang SC", "PingFang SC", "Source Han Sans SC", "Noto Sans CJK SC", "Source Han Sans CN", "Noto Sans SC", "Source Han Sans TC", "Noto Sans CJK TC", "Hiragino Sans GB", sans-serif;
    font-size: 14px;
    background-color: #f5f5f5;
    line-height: 26px;
    color: #2B415C;
    -webkit-font-smoothing: antialiased;
    margin: 0;
  }

  .form .el-input > .el-input__inner {
    border-radius: 0;
  }
</style>

