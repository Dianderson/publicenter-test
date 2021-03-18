<template>
  <div class="q-pa-md bg-blue-grey-1" align="center">
    <q-layout view="lHh lpr lFf" container style="width: 98%;height: calc(800px - 85px)"
              class="shadow-2 rounded-borders">
      <q-header bordered class="bg-blue-grey-5">
        <q-toolbar-title style="text-align: center">
          Pesquisa de município
        </q-toolbar-title>
      </q-header>

      <q-page-container align="center">
        <q-page padding class="bg-blue-grey-1" style="margin-top: -25px">

          <div class=" row flex-center" style="width: 700px">
            <q-input v-model="cityName" label="Nome" style="width: 300px;margin-inline: 20px"/>
            <q-select v-model="stateName" label="Estado" style="width: 200px;margin-inline: 20px"
                      :options="statesList"
                      :option-value="opt => opt.name"
                      :option-label="opt => opt.name"
            />
          </div>

          <div style="margin-top: 25px">
            <q-btn label="Pesquisar" type="submit" class="bg-blue-grey-4" color="white"
                   style="width: 150px;height: 35px;margin-inline: 30px" icon="search" @click="findCity"/>

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
                           @click="updateCity(props.row.id,props.row.name,props.row.state.id,props.row.state.name)"/>
                    <q-btn flat dense color="white" icon="delete"
                           @click="deleteCity(props.row.id,props.row.name,props.row.state.name)"/>
                  </q-td>
                </q-tr>
              </template>
            </q-table>
          </div>

          <q-dialog v-model="confirmUpdate" persistent>
            <q-card>
              <q-card-section class="bg-blue-grey-1 row items-center">
                <div style="min-width: 100%">
                  <q-input class="bg-blue-grey-1" label="id:" v-model="cityId"
                           style="font-size: 18px; width: 40px" readonly/>
                  <q-input class="bg-blue-grey-1" label="Nome:" v-model="cityName"
                           style="font-size: 20px"
                           :rules="[val =>
                    (val && val.length > 0) || 'O campo deve estar preenchido']"/>
                  <q-input class="bg-blue-grey-1" label="Estado:" v-model="stateName"
                           style="font-size: 20px"
                           :rules="[val =>
                    (val && val.length > 0) || 'O campo deve estar preenchido']"/>
                </div>
              </q-card-section>
              <q-card-actions class="bg-blue-grey-2" align="center">
                <q-btn flat label="Salvar" color="blue-grey-8" v-close-popup
                       style="margin-inline: 50px; font-weight: bold; width: 100px" @click="updateCityConfirm"></q-btn>
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
                {{ this.cityId + ' - ' + this.cityName + ' / ' + this.stateName }}
              </div>
              <q-card-actions class="bg-blue-grey-2" align="center">
                <q-btn flat label="Confirmar" color="red-5" v-close-popup
                       style="margin-inline: 50px; font-weight: bold; width: 100px" @click="deleteCityConfirm"></q-btn>
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
      cityId: '',
      cityName: '',
      stateId: '',
      stateName: '',
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
          style: 'width: 50%'
        },
        {
          name: 'stateName',
          align: 'left',
          label: 'Estado',
          field: row => row.state.name,
          sortable: true,
          style: 'width: 35%'
        }
      ],
      rows: [],
      statesList: []
    }
  },
  mounted () {
    const method = 'get'
    const url = 'http://localhost:8080/state'
    this.genericRequest('get', url).then(response => {
        this.statesList = response.data.content
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

    findCity () {
      let url = ''
      if (this.cityName !== '' && this.stateName === '') {
        url = `http://localhost:8080/city/find-by-name?name=${this.cityName}`
      } else if (this.cityName === '' && this.stateName !== '') {
        url = `http://localhost:8080/city/find-by-state?stateId=${this.stateName.id}`
      } else if (this.cityName !== '' && this.stateName !== '') {
        url = `http://localhost:8080/city/find-by-parameters?name=${this.cityName}&stateId=${this.stateName.id}`
      }else {
        url = `http://localhost:8080/city`
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
            this.$router.push('/login')
          }
        }
      )
    },

    updateCity (id, name, stateId, stateName) {
      this.cityId = id
      this.cityName = name
      this.stateId = stateId
      this.stateName = stateName
      this.confirmUpdate = true
    },

    updateCityConfirm () {
      const method = 'put'
      const url = 'http://localhost:8080/city/' + this.cityId
      const dataRequest = {
        name: this.cityName,
        stateId: this.stateId
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

    deleteCity (id, name, stateName) {
      this.cityId = id
      this.cityName = name
      this.stateName = stateName
      this.confirmDelete = true
    },

    deleteCityConfirm () {
      const method = 'delete'
      const url = 'http://localhost:8080/city/' + this.cityId
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
      this.cityName = ''
      this.stateName = ''
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
