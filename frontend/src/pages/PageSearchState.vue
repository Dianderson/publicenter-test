<template>
  <div class="q-pa-md bg-blue-grey-1" align="center">
    <q-layout view="lHh lpr lFf" container style="width: 98%;height: calc(800px - 85px)"
              class="shadow-2 rounded-borders">
      <q-header bordered class="bg-blue-grey-5">
        <q-toolbar-title style="text-align: center">
          Pesquisa de estado
        </q-toolbar-title>
      </q-header>

      <q-page-container align="center">
        <q-page padding class="bg-blue-grey-1" style="margin-top: -25px">

          <div class=" row flex-center">
            <q-input v-model="stateName" label="Nome" style="width: 300px ;margin-inline: 20px"/>
            <q-input v-model="stateCode" label="Sigla" style=" width: 100px;margin-inline: 20px"/>
          </div>

          <div style="margin-top: 25px">
            <q-btn label="Pesquisar" type="submit" class="bg-blue-grey-4" color="white"
                   style="width: 150px;height: 35px;margin-inline: 30px" icon="search" @click="findStates"/>

            <q-btn label="Limpar" type="reset" class="bg-blue-grey-4" color="white"
                   style="width: 135px;height: 35px;margin-inline: 30px" icon="clear" @click="clear"/>
          </div>

          <div class="q-pa-md" style="max-width: 800px">
            <q-table
              class="bg-blue-grey-2"
              :data="rows"
              :columns="columns"
              row-key="name"
              :pagination.sync="pagination"
              hide-pagination
              hide-no-data
              separator="cell"
            >
              <template v-slot:body="props">
                <q-tr :props="props">
                  <q-td
                    v-for="col in props.cols"
                    :key="col.name"
                    :props="props"
                  >
                    {{ col.value }}
                  </q-td>
                  <q-td class="bg-blue-grey-3" auto-width>
                    <q-btn flat dense color="white" icon="edit"
                           @click="updateState(props.row.id,props.row.name,props.row.code)"/>
                    <q-btn flat dense color="white" icon="delete"
                           @click="deleteState(props.row.id,props.row.name,props.row.code)"/>
                  </q-td>
                </q-tr>
              </template>
            </q-table>
          </div>

          <q-dialog v-model="confirmUpdate" persistent>
            <q-card>
              <q-card-section class="bg-blue-grey-1 row items-center">
                <div style="min-width: 100%">
                  <q-input class="bg-blue-grey-1" label="id:" v-model="stateId"
                           style="font-size: 18px; width: 40px" readonly/>
                  <q-input class="bg-blue-grey-1" label="Sigla:" v-model="stateCode"
                           style="font-size: 20px"
                           :rules="[val =>
                    (val && val.length > 0) || 'O campo deve estar preenchido']"/>
                  <q-input class="bg-blue-grey-1" label="Nome:" v-model="stateName"
                           style="font-size: 20px"
                           :rules="[val =>
                    (val && val.length > 0) || 'O campo deve estar preenchido']"/>
                </div>
              </q-card-section>
              <q-card-actions class="bg-blue-grey-2" align="center">
                <q-btn flat label="Salvar" color="blue-grey-8" v-close-popup
                       style="margin-inline: 50px; font-weight: bold; width: 100px" @click="updateStateConfirm"></q-btn>
                <q-btn flat label="Cancelar" color="blue-grey-8" v-close-popup
                       style="margin-inline: 50px; font-weight: bold; width: 100px"></q-btn>
              </q-card-actions>
            </q-card>
          </q-dialog>

          <q-dialog v-model="confirmDelete" persistent>
            <q-card>
              <q-card-section class="bg-blue-grey-1 row items-center">
                <q-avatar icon="warning" color="red-5" text-color="white"></q-avatar>
                <span class="q-ml-sm" style="font-size: 20px;margin-inline: 30px">Deseja mesmo excluir o registro abaixo?</span>
              </q-card-section>
              <div class="bg-blue-grey-1" style="font-size: 20px; text-align: center">
                {{ this.stateId + ' - ' + this.stateName + ' / ' + this.stateCode }}
              </div>
              <q-card-actions class="bg-blue-grey-2" align="center">
                <q-btn flat label="Confirmar" color="red-5" v-close-popup
                       style="margin-inline: 50px; font-weight: bold; width: 100px" @click="deleteStateConfirm"></q-btn>
                <q-btn flat label="Cancelar" color="red-5" v-close-popup
                       style="margin-inline: 50px; font-weight: bold; width: 100px"></q-btn>
              </q-card-actions>
            </q-card>
          </q-dialog>

          <q-footer elevated class="bg-blue-grey-7">
            <q-pagination
              v-model="pagination.page"
              class="flex-center"
              color="white"
              :max="pagesNumber"
              input-class="text-bold"
              :input="true"
            />
          </q-footer>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PageSearchState',
  data () {
    return {
      stateId: '',
      stateName: '',
      stateCode: '',
      confirmUpdate: false,
      confirmDelete: false,

      pagination: {
        sortBy: 'name',
        descending: false,
        page: 1,
        rowsPerPage: 10
      },
      columns: [
        {
          name: 'id',
          required: true,
          align: 'center',
          label: 'Código',
          field: 'id',
          sortable: true,
          style: 'width: 15%'
        },
        {
          name: 'name',
          align: 'left',
          label: 'Nome',
          field: 'name',
          sortable: true,
          style: 'width: 65%'
        },
        {
          name: 'code',
          align: 'center',
          label: 'Sigla',
          field: 'code',
          sortable: true,
          style: 'width: 20%'
        }
      ],
      rows: [],
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

    findStates () {
      let url = ''
      if (this.stateName !== '' && this.stateCode === '') {
        url = `http://localhost:8080/state/find-by-name?name=${this.stateName}`
      } else if (this.stateName === '' && this.stateCode !== '') {
        url = `http://localhost:8080/state/find-by-code?code=${this.stateCode}`
      } else {
        url = `http://localhost:8080/state/find-by-parameters?name=${this.stateName}&code=${this.stateCode}`
      }

      this.genericRequest('get', url).then(response => {
          this.rows = response.data.content
        }, (error) => {
          if (error.response.data.status === 403) {
            this.$q.notify({
              color: 'red-5',
              textColor: 'white',
              icon: 'warning',
              message: error.response.data.message
            })
          }
          this.$router.push('/login')
        }
      )
    },

    updateState (id, name, code) {
      this.stateId = id
      this.stateName = name
      this.stateCode = code
      this.confirmUpdate = true
    },

    updateStateConfirm () {
      const method = 'put'
      const url = 'http://localhost:8080/state/' + this.stateId
      const dataRequest = {
        name: this.stateName,
        code: this.stateCode
      }
      this.genericRequest(method, url, dataRequest).then(response => {
        if (response.status === 200) {
          this.$q.notify({
            color: 'green-5',
            textColor: 'white',
            icon: 'warning',
            message: 'Registro alterado com sucesso.'
          })
        }
      }, (error) => {
        if (error.response.data.httpStatus === 409) {
          this.$q.notify({
            color: 'red-5',
            textColor: 'white',
            icon: 'warning',
            message: 'O sistema já possui um registro com os campos informado.'
          })
        }
      })
      this.clear()
    },

    deleteState (id, name, code) {
      this.stateId = id
      this.stateName = name
      this.stateCode = code
      this.confirmDelete = true
    },

    deleteStateConfirm () {
      const method = 'delete'
      const url = 'http://localhost:8080/state/' + this.stateId
      this.genericRequest(method, url).then(response => {
        if (response.status === 200) {
          this.$q.notify({
            color: 'green-5',
            textColor: 'white',
            icon: 'warning',
            message: 'Registro excluído com sucesso.'
          })
        }
      }, (error) => {
        if (error.response.data.httpStatus === 409) {
          this.$q.notify({
            color: 'red-5',
            textColor: 'white',
            icon: 'warning',
            message: 'Este registro não pode ser excluído, pois possui um relacionamento.'
          })
        }
      })
      this.clear()
    },

    clear () {
      this.stateName = ''
      this.stateCode = ''
      this.rows = []
    }
  },
  computed: {
    pagesNumber () {
      return Math.ceil(this.rows.length / this.pagination.rowsPerPage)
    }
  }
}
</script>

<style>

</style>
