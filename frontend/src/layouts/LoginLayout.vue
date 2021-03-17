<template>
  <div class="q-pa-md">
    <q-layout
      view="lHh Lpr lff"
      container
      style="min-height: 800px"
      class="shadow-2 rounded-borders"
    >

      <q-header elevated class="bg-blue-grey-7">
        <q-toolbar>
          <q-toolbar-title style="text-align: right">Publicenter Sistemas</q-toolbar-title>
        </q-toolbar>
      </q-header>

      <q-page-container>
        <router-view/>
      </q-page-container>
    </q-layout>
  </div>
</template>
