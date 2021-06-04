
const consultarproyecto = async () => {  
    
    const option = 1
    document.getElementById('mensaje').innerHTML= ""
    document.getElementById('nuevoboton').innerHTML= 
    ` 
    <button type="button" data-toggle="modal" data-target="#modal-1" style="background: #0A2269; border-color: #E6E6E6; width:130px" id="nuevoboton" class="col-1 bt btn-primary
        btn-block"
        >Nuevo Proyecto
      </button> 
      <p></p> 
    `
    
    document.getElementById('div-modal').innerHTML =
    ` 
    <div class="modal fade" id="modal-1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"> 
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">Formulario Crear Proyecto</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                    <div> 
                        <h6>Título</h6> 
                        <input id="titulo" type="text" class="form-control mb-3" placeholder="Título" rows="2">
                    </div> 
                    <div> 
                        <h6>Objetivo</h6> 
                        <textarea id="objetivo" class="form-control mb-3" placeholder="Objetivo" rows="4" style="height:100%"></textarea>
                    </div> 
                    <div>
                        <h6>Descripción</h6>
                        <textarea id="descripcion" class="form-control mb-3" placeholder="Descripción" rows="4" style="height:100%"></textarea>
                    </div>
                    <div>
                        <h6>Presupuesto</h6> 
                        <input id="presupuesto" type="text" class="form-control mb-3" placeholder="Presupuesto">
                    </div>
                    <div>
                        <h6>Fecha Inicio</h6>
                        <input id="fechainicio" type="text" class="form-control mb-3" placeholder="Fecha Inicio"> 
                    </div> 
                    <div> 
                        <h6>Fecha Fin</h6> 
                        <input id="fechafin" type="text" class="form-control mb-3" placeholder="Fecha Fin">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal"
                            onclick="agregarproyecto()">Guardar
                    </button>
                </div>
            </div>
        </div>    
    </div>
    `
    document.getElementById('div-modal-2').innerHTML =
    ` 
    <div class="modal fade" id="modal-2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="false"> 
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">Formulario Editar Proyecto</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                    <div> 
                        <h6>Título</h6> 
                        <input id="titulo-2" type="text" class="form-control mb-3">
                    </div> 
                    <div> 
                        <h6>Objetivo</h6> 
                        <textarea id="objetivo-2" class="form-control mb-3" rows="4" style="height:100%"></textarea>
                    </div> 
                    <div>
                        <h6>Descripción</h6>
                        <textarea id="descripcion-2" class="form-control mb-3" rows="4" style="height:100%"></textarea>
                    </div>
                    <div>
                        <h6>Presupuesto</h6> 
                        <input id="presupuesto-2" type="text" class="form-control mb-3">
                    </div>
                    <div>
                        <h6>Fecha Inicio</h6>
                        <input id="fechainicio-2" type="text" class="form-control mb-3"> 
                    </div> 
                    <div> 
                        <h6>Fecha Fin</h6> 
                        <input id="fechafin-2" type="text" class="form-control mb-3">
                    </div>
                    <input type="hidden" id="idproyecto">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal"
                            onclick="editarproyecto()">Editar
                    </button>
                </div>
            </div>
        </div>    
    </div>
    `
    document.getElementById('div-info').innerHTML = 
    `
    <div class="modal fade" id="modal-info" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Información Completa del Proyecto</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                     
                    <div> 
                        <h6>Objetivo</h6> 
                        <p id="objetivo-3"></p>
                    </div> 
                    <div>
                        <h6>Descripción</h6>
                        <p id="descripcion-3"></p>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <h6>Presupuesto</h6> 
                            <p id="presupuesto-3"></p>
                        </div>
                        <div class="col-md-4">
                            <h6>Fecha Inicio</h6>
                            <p id="fechainicio-3"></p>
                        </div>
                        <div class="col-md-4">
                            <h6>Fecha Fin</h6> 
                            <p id="fechafin-3"></p>
                        </div>
                        </div>
                    <div> 
                        <h6>Interesado</h6> 
                        <p id="interesado"></p>
                    </div> 
                    <div> 
                        <h6>Ciclo Vida Proyecto</h6> 
                        <p id="cvproyecto"></p>
                    </div>
                    <div> 
                        <h6>Entregable</h6> 
                        <p id="entregable"></p>
                    </div>
                    <div> 
                        <h6>Número Procesos</h6> 
                        <p id="numprocesos"></p>
                    </div>
                    <div> 
                        <h6>Descripción RDS</h6> 
                        <p id="desrds"></p>
                    </div>
                    <div> 
                        <h6>Descripción Riesgo</h6> 
                        <p id="desriesgo"></p>
                    </div>
                    <div> 
                        <h6>Descripción RI</h6> 
                        <p id="desri"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal"
                            style="background: #0A2269; border-color: #E6E6E6">Cerrar</button>
                    
                </div>
            </div>
        </div>
    </div>
    `
              
    buscandoproyecto()
} 

const buscandoproyecto = async () => {
    //document.getElementById('mensaje').innerHTML= "Proyectos encontrados"
    const option = 1
    const titulo = ""
    const objetivo = ""
    const descripcion = ""
    const fechainicio = ""
    const fechafin = ""
    const presupuesto = ""
    const id = ""
    /*const titulo = document.getElementById('titulo')
    const objetivo = document.getElementById('objetivo')
    const descripcion = document.getElementById('descripcion')
    const fechainicio = document.getElementById('fechainicio')
    const fechafin = document.getElementById('fechafin')
    const presupuesto = document.getElementById('presupuesto')*/
    const queryParams = `?option=${option}&atributo1=${titulo}&atributo2=${objetivo}&atributo3=${descripcion}&atributo4=${fechainicio}&atributo5=${fechafin}&atributo6=${presupuesto}&atributo7=${id}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        //const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const resultDiv = document.getElementById('result')
        let stringHtml = '<div class="alert alert-primary" role="alert">Sin resultados</div>'
        if (data.length > 0) 
        {  
        const items = data.map(item =>  
        `
        
        <tr>
            <td style="text-align: left" data-placement="top" title="Título">${item.titulo}</td>
            <!-- comment <td data-placement="top" title="Objetivo">${item.objetivo}</td>
            <td data-placement="top" title="Descripción">${item.descripcion}</td>
            <td data-placement="top" title="Presupuesto">${item.presupuesto}</td> -->
            <td data-placement="top" title="Fecha Inicio">${item.fechainicio}</td>
            <td data-placement="top" title="Fecha Fin" contenteditable="false">${item.fechafin}</td> 
            
            <td>
                
                    <div class="btn-group">
                        <button class="btn btn-info btn-sm rounded-0" type="button" 
                                style="background: #0A2269; border-color: #FFFFFF"
                                data-toggle="modal" data-placement="right" title="Info" 
                                data-original-title="Info" data-target="#modal-info" onclick="mostrarinfo('${item.idproyecto}','${item.objetivo}','${item.descripcion}','${item.presupuesto}','${item.fechainicio}','${item.fechafin}')"><i class="fas fa-info-circle"></i>
                        </button>
                    
                    
                        <button class="btn btn-success btn-sm rounded-0" type="button" 
                                style="background: #0A2269; border-color: #FFFFFF"
                                data-toggle="modal" data-placement="right" title="Editar" 
                                data-original-title="Edit" data-target="#modal-2" onclick="mostrareditar('${item.idproyecto}','${item.titulo}','${item.objetivo}','${item.descripcion}','${item.fechainicio}','${item.fechafin}','${item.presupuesto}')"><i class="fas fa-edit"></i>
                        </button> 
                    
                    
                        <button class="btn btn-danger btn-sm rounded-0" type="button"
                                style="background: #0A2269; border-color: #FFFFFF"
                                data-toggle="tooltip" data-placement="right" title="Eliminar" 
                                data-original-title="Delete" onclick="eliminarproyecto('${item.idproyecto}')"><i class="fas fa-trash"></i>
                        </button>
                    </div>
            </td>
            
        </tr>
        
            
        
            <tr class="collapse multi-collapse" id="collapseExample${item.idproyecto}">
                
                <th class= "col-2" scope="col">Interesado</th>
                <th class= "col-2" scope="col">CVProyecto</th>
                <th class= "col-1" scope="col">Entregable</th>
                <th class= "col-1" scope="col">Número Procesos</th>
                <th class= "col-2" scope="col">Descripción RDS</th>
                <th class= "col-2" scope="col">Descripción Riesgo</th>
                <th class= "col-2" scope="col">Descripción RI</th>
                
            </tr>
            
            <tr class="collapse multi-collapse" id="collapseExample${item.idproyecto}">
                <td data-placement="top" title="Título">${item.titulo}</td>
                <td data-placement="top" title="Objetivo">${item.objetivo}</td>
                <td data-placement="top" title="Descripción">${item.descripcion}</td>
                <td data-placement="top" title="Presupuesto">${item.presupuesto}</td> 
                <td data-placement="top" title="Fecha Inicio">${item.fechainicio}</td>
                <td data-placement="top" title="Fecha Fin" contenteditable="false">${item.fechafin}</td>
                <td data-placement="top" title="Fecha Fin" contenteditable="false">hola</td>
            </tr>
           
            
        
        
        
        `
        ).join('')
        stringHtml =
        `
        <table class="table-editable table table-bordered table-responsive-md table-striped text-center">
            <thead>
                <tr>
                    <th class= "col-8" scope="col">Título</th>
                    <!-- comment <th class= "col-4" scope="col">Objetivo</th>
                    <th class= "col-6" scope="col">Descripción</th>
                    <th class= "col-2" scope="col">Presupuesto</th> -->
                    <th class= "col-1" scope="col">Fecha Inicio</th>
                    <th class= "col-1" scope="col">Fecha Fin</th> 
                    <th class= "col-1" scope="col">Acción</th> 
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>
        `
        }
        console.log("Entro a buscandoproyecto",data)
    resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    }
}

const agregarproyecto = async () => {
    console.log("entrando a agregarproyecto");
    const option = 2
    
    const titulo = document.getElementById('titulo').value
    const objetivo = document.getElementById('objetivo').value
    const descripcion = document.getElementById('descripcion').value
    const fechainicio = document.getElementById('fechainicio').value
    const fechafin = document.getElementById('fechafin').value
    const presupuesto = document.getElementById('presupuesto').value
    const id = ""
    const queryParams = `?option=${option}&atributo1=${titulo}&atributo2=${objetivo}&atributo3=${descripcion}&atributo4=${fechainicio}&atributo5=${fechafin}&atributo6=${presupuesto}&atributo7=${id}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        //const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        console.log("Entro a agregarproyecto",data)
        buscandoproyecto()
    }
    catch (err) 
    { 
        console.log(err)
    }
    document.getElementById('titulo').value = ""
    document.getElementById('objetivo').value = ""
    document.getElementById('descripcion').value = ""
    document.getElementById('fechainicio').value = ""
    document.getElementById('fechafin').value = ""
    document.getElementById('presupuesto').value = ""
}

const editarproyecto = async (aux) => {
    console.log("entrando a editarproyecto");
    const option = 3
    const titulo = document.getElementById('titulo-2').value
    const objetivo = document.getElementById('objetivo-2').value
    const descripcion = document.getElementById('descripcion-2').value
    const fechainicio = document.getElementById('fechainicio-2').value
    const fechafin = document.getElementById('fechafin-2').value
    const presupuesto = document.getElementById('presupuesto-2').value
    const id = document.getElementById('idproyecto').value
    const queryParams = `?option=${option}&atributo1=${titulo}&atributo2=${objetivo}&atributo3=${descripcion}&atributo4=${fechainicio}&atributo5=${fechafin}&atributo6=${presupuesto}&atributo7=${id}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        //const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        console.log("Entro a editar",data)
        buscandoproyecto()
    }
    catch (err) 
    { 
        console.log(err)
    }
    
}

const mostrareditar = async(aux1,aux2,aux3,aux4,aux5,aux6,aux7) => {
    console.log("entrando a mostrareditar ",aux7);
    //
    document.getElementById('titulo-2').value = aux2
    document.getElementById('objetivo-2').value = aux3
    document.getElementById('descripcion-2').value = aux4
    document.getElementById('fechainicio-2').value = aux5
    document.getElementById('fechafin-2').value = aux6
    document.getElementById('presupuesto-2').value= aux7
    document.getElementById('idproyecto').value = aux1
    //limpiarmodal()
    
}

const limpiarmodal = async() => {
    document.getElementById('titulo').value = ""
    document.getElementById('objetivo').value = ""
    document.getElementById('descripcion').value = ""
    document.getElementById('fechainicio').value = ""
    document.getElementById('fechafin').value = ""
    document.getElementById('presupuesto').value= ""
    const id =""
}

const eliminarproyecto = async(aux) => {
    console.log("entrando a eliminarproyecto",aux);
    if(confirm('¿Està seguro de borrar este proyecto?'))
    {
    const option = 4
    const titulo = ""
    const objetivo = ""
    const descripcion = ""
    const fechainicio = ""
    const fechafin = ""
    const presupuesto = ""
    const id = aux
    const queryParams = `?option=${option}&atributo1=${titulo}&atributo2=${objetivo}&atributo3=${descripcion}&atributo4=${fechainicio}&atributo5=${fechafin}&atributo6=${presupuesto}&atributo7=${id}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        //const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        console.log("Entro a eliminarproyecto",data)
        buscandoproyecto()
    }
    catch (err) 
    { 
        console.log(err)
    }
    }
}

const mostrarinfo = async(aux,aux2,aux3,aux4,aux5,aux6) => {
    console.log("Mostrando toda la info ",aux);
    //document.getElementById('titulo-3').innerHTML = "Aquí va el titulo"
    document.getElementById('objetivo-3').innerHTML = aux2
    document.getElementById('descripcion-3').innerHTML = aux3
    document.getElementById('presupuesto-3').innerHTML = aux4
    document.getElementById('fechainicio-3').innerHTML = aux5
    document.getElementById('fechafin-3').innerHTML = aux6
    document.getElementById('interesado').innerHTML = aux2
    document.getElementById('cvproyecto').innerHTML = aux3
    document.getElementById('entregable').innerHTML = aux2
    document.getElementById('numprocesos').innerHTML = aux3
    document.getElementById('desrds').innerHTML = aux2
    document.getElementById('desriesgo').innerHTML = aux3
    document.getElementById('desri').innerHTML = aux2+('<br></br>')+aux3
}


