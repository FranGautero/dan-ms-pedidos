package com.labs.danmspedidos.rest;

import com.labs.danmspedidos.model.DetallePedido;
import com.labs.danmspedidos.model.Pedido;
import com.labs.danmspedidos.model.Obra;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/pedido")
@Api(value = "PedidoRest", description = "Permite gestionar los pedidos de la empresa")
public class PedidoRest {


    private static final List<Pedido> listaPedidos = new ArrayList<>();
    private static Integer ID_GEN = 1;

    //POST Pedido nuevo

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido nuevo){
        System.out.println(" crear empleado "+nuevo);
        nuevo.setId(ID_GEN++);
        listaPedidos.add(nuevo);
        return ResponseEntity.ok(nuevo);
    }

    //POST Item al Pedido
    @PostMapping(path = "{idPedido}/detalle")
    public ResponseEntity<DetallePedido> agregarItem(@RequestBody DetallePedido detallePedido, @PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaPedidos.get(indexOpt.getAsInt()).getDetallePedido().add(detallePedido);
            return ResponseEntity.ok(detallePedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza un Pedido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizado correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Pedido> actualizar(@RequestBody Pedido nuevo, @PathVariable("id") Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaPedidos.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //DETELE un Pedido por ID

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Pedido> borrar(@PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaPedidos.remove(indexOpt.getAsInt());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //DETELE un Detalle de un Pedido

    @DeleteMapping(path = "/{idPedido}/detalle/{id}")
    public ResponseEntity<DetallePedido> borrarDetalle(@PathVariable("idPedido") Integer idPedido, @PathVariable("id") Integer idDetalle){
        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(idPedido))
                .findFirst();

        if(indexOpt.isPresent()){
            OptionalInt indexOpt2 =   IntStream.range(0, listaPedidos.get(indexOpt.getAsInt()).getDetallePedido().size())
                    .filter(i -> listaPedidos.get(indexOpt.getAsInt()).getDetallePedido().get(i).getId().equals(idDetalle))
                    .findFirst();
            if(indexOpt2.isPresent()){
                listaPedidos.get(indexOpt.getAsInt()).getDetallePedido().remove(indexOpt2.getAsInt());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //GET Pedido por ID

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un Pedido por id")
    public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer id){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(unCli -> unCli.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }

    //GET Pedido por ID de Obra

    @GetMapping(path = "/{idObra}")
    @ApiOperation(value = "Busca un Pedido por ID de Obra")
    public ResponseEntity<Pedido> pedidoPorIDObra(@PathVariable Integer id){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(unCli -> unCli.getObra().getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }








}
