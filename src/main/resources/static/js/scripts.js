$(document).ready(function() {
    $(".navbar__item").hover(function() {
        $(this).find(".submenu").css("display", "block");
    }, function() {
        $(this).find(".submenu").css("display", "none");
    });

    $(".submenu__link").click(function() {
        $(this).closest(".submenu").css("display", "none");
    });

    $("#filtroCategoriaForm").submit(function(e) {
        e.preventDefault();
        var idCategoria = $("#idCategoria").val();
        filtrarPorCategoria(idCategoria);
    });

  $(".editar-vehiculo").click(function() {
    var href = $(this).attr("href");
    if (href) {
        window.location.href = href;
    } else {
        console.error("Enlace de edición no válido");
    }
    return false; // Agrega esta línea para evitar que el formulario se envíe automáticamente
});

});

function filtrarPorCategoria(idCategoria) {
    window.location.href = "/vehiculos/filtrarPorCategoria?idCategoria=" + idCategoria;
}
