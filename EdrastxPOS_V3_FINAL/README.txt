EdrastxPOS - Versión 3

Main JavaFX:
  com.edrastx.pos.MainApp

Consola de prueba:
  com.edrastx.pos.SysCore

Usuarios:
  ADMIN:
    usuario: estrada
    clave  : edrast

  VENDEDOR:
    usuario: ventas
    clave  : venta12

Base de datos:
  SQLite en carpeta /data/sysventas.db
  Se crea automáticamente al iniciar MainApp o SysCore.

Funciones:
  - Login con roles
  - CRUD Productos (solo ADMIN)
  - CRUD Clientes (solo ADMIN)
  - Ventas con carrito y control de stock
  - Nombre de cliente libre o desde listado
  - Ticket con logo, impresión automática
  - Botón "Test impresión" en pantalla de ventas
  - Reporte Excel de ventas del día (solo ADMIN)

Reporte Excel:
  Botón "Reporte del día (Excel)" en menú lateral (solo admin).
  Genera archivo en carpeta /data con nombre: ventas_YYYY-MM-DD.xlsx
