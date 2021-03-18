<template>
  <div class="q-pa-md bg-blue-grey-1" align="center">
    <q-layout view="lHh lpr lFf" container style="width: 98%;height: calc(800px - 85px)"
              class="shadow-2 rounded-borders">
      <q-header bordered class="bg-blue-grey-5">
        <q-toolbar-title style="text-align: center">
          Cadastro de estado
        </q-toolbar-title>
      </q-header>

      <q-page-container align="center">
        <q-page padding class="bg-blue-grey-1" style="margin-top: -25px">
          <q-form @submit="saveState" @reset="clear" class="q-gutter-md">
            <div class=" row flex-center" style="margin-top: 3%">
              <q-input v-model="stateName" label="Nome" style="width: 300px ;margin-inline: 20px"
                       :rules="[val =>(val && val.length > 0) || 'O campo deve estar preenchido']"/>
              <q-input v-model="stateCode" label="Sigla" style=" width: 100px;margin-inline: 20px"
                       :rules="[val =>(val && val.length > 0) || 'O campo deve estar preenchido']"/>
            </div>

            <div style="margin-top: 2%">
              <q-btn label="Salvar" type="submit" class="bg-blue-grey-4" color="white"
                     style="width: 150px;height: 35px;margin-inline: 30px" icon="save"/>

              <q-btn label="Limpar" type="reset" class="bg-blue-grey-4" color="white"
                     style="width: 150px;height: 35px;margin-inline: 30px" icon="clear"/>
            </div>
          </q-form>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PageCreateState',
  data () {
    return {
      stateName: '',
      stateCode: '',
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

    saveState () {
      const method = 'post'
      const url = 'http://localhost:8080/state'
      const dataRequest = {
        name: this.stateName,
        code: this.stateCode
      }
      this.genericRequest(method, url, dataRequest).then(response => {
        if (response.status === 201) {
          this.$q.notify({
            color: 'green-5',
            textColor: 'white',
            icon: 'warning',
            message: 'Registro incluído com sucesso.'
          })
        }
      }, (error) => {
        if (error.response.data.httpStatus === 400) {
          this.$q.notify({
            color: 'red-5',
            textColor: 'white',
            icon: 'warning',
            message: 'Favor corrigir o(s) campo(s) inválido(s).'
          })
        } else if (error.response.data.httpStatus === 409) {
          this.$q.notify({
            color: 'red-5',
            textColor: 'white',
            icon: 'warning',
            message: 'O sistema já possui um registro com os campos informado.'
          })
        }
      })
    },

    clear () {
      this.stateName = ''
      this.stateCode = ''
    }
  }
}
</script>

<style>

</style>
