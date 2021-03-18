<template>
  <q-page>
    <div class="q-pa-md">
      <q-layout
        view="lHh lpr lFf"
        container
        style="height: calc(800px - 90px)"
        class="shadow-2 rounded-borders"
      >
        <q-header bordered class="bg-blue-grey-5">
          <q-toolbar-title style="text-align: center">
            Login
          </q-toolbar-title>
        </q-header>

        <q-page-container pading style="margin-top:40px" align="center">
          <div class="q-pa-md" style="max-width: 400px">
            <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
              <q-input
                v-model="userName"
                label="Usuario"
                :rules="[
                  val =>
                    (val && val.length > 0) || 'Insira um usuário cadastrado'
                ]"
              />

              <q-input
                v-model="password"
                label="Senha"
                :type="isPwd ? 'password' : 'text'"
                :rules="[
                  val => (val && val.length > 0) || 'Insira uma senha válida'
                ]"
              >
                <template v-slot:append>
                  <q-icon
                    :name="isPwd ? 'visibility_off' : 'visibility'"
                    class="cursor-pointer"
                    @click="isPwd = !isPwd"
                  />
                </template>
              </q-input>

              <div class="form-group row">
                <div class="col-md-6">
                  <q-btn
                    label="Acessar"
                    type="submit"
                    class="bg-blue-grey-4"
                    color="width"
                    style="width: 150px"
                  />
                </div>
                <div class="col-md-6">
                  <q-btn
                    label="Reset"
                    type="reset"
                    class="bg-blue-grey-4"
                    color="width"
                    style="width: 150px"
                  />
                </div>
              </div>
            </q-form>
          </div>
        </q-page-container>
      </q-layout>
    </div>
  </q-page>
</template>

<script>
import axios from 'axios'

export default {
  data () {
    return {
      userName: '',
      password: '',
      isPwd: true
    }
  },
  mounted () {
  },

  methods: {
    genericRequest (method, url, dataRequest) {
      const instance = axios.create({
        method: method,
        baseURL: url,
        data: dataRequest,
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('user-token') }
      })
      return instance()
    },

    onSubmit () {
      const method = 'post'
      const url = 'http://localhost:8080/auth'
      const dataRequest = {
        username: this.userName,
        password: this.password
      }

      this.genericRequest(method, url, dataRequest).then(response => {
        localStorage.setItem('user-name', response.data.username)
        localStorage.setItem('user-token', response.data.token)
        localStorage.setItem('user-role', response.data.roleDescription)
        this.$router.push('/main')
      }).catch(error => {
        this.$q.notify({
          color: 'red-5',
          textColor: 'white',
          icon: 'warning',
          message: 'Dados inválidos'
        })
      })
    },
    onReset () {
      this.userName = null
      this.password = null
    }
  }
}
</script>
