# language:pt
  @Checkout
  Funcionalidade: Advantage Website
    Eu como cliente quero realizar o checkout
    Para que eu possa comprar diferentes produtos no website

  @AddProdToCart @Auth
  Cenario: Adicionar produtos ao carrinho
    Dado que adiciono lista de produtos ao carrinho de compras
      |id| produto                                         | quantidade | cor              | valor   |colorCode|
      |15| Beats Studio 2 Over-Ear Matte Black Headphones  | 1          | rgb(65, 65, 65)  | 179.99  |414141   |
      |30| HP Z4000 Wireless Mouse	                       | 2          | rgb(221, 58, 91) | 9.99    |DD3A5B   |
    Quando navego até o carrinho de compras
    Entao verifico que a lista de produtos foram adicionadas corretamente
      E verifico que o valor total da compra está correto

  @RemoveProdFromCart @Auth
  Cenario: Remover items do carrinho de compras
    Dado que adiciono lista de produtos ao carrinho de compras
      |id| produto                                          | quantidade | cor              | valor   |codeColor|
      |15| Beats Studio 2 Over-Ear Matte Black Headphones   | 1          | rgb(65, 65, 65)  | 179.99  |414141   |
      |30| HP Z4000 Wireless Mouse	                        | 2          | rgb(221, 58, 91) | 9.99    |DD3A5B   |
    Quando navego até o carrinho de compras
      E removo produtos adicionados
    Entao verfico a mensagem "Your shopping cart is empty"

    @EditCartsProd @Auth
    Cenario: Editar Produto no carrinho de compras
    Dado que adiciono lista de produtos ao carrinho de compras
      |id| produto                                          | quantidade | cor                | valor   |codeColor|
      |15| HP Z4000 WIRELESS MOUSE                          | 1          | rgb(54, 131, 209)  | 9.99    |ABCDEF   |
    Quando navego até o carrinho de compras
      E edito o produto adicionado pelo carrinho de compras
        |id| produto                                          | quantidade | cor                | valor   |codeColor|
        |15| HP Z4000 WIRELESS MOUSE                          | 2          | rgb(84, 81, 149)   | 9.99    |ABCDEF   |
      E navego até o carrinho de compras
    Entao verifico que a lista de produtos foram adicionadas corretamente
      E verifico que o valor total da compra está correto

    @FullCheckout @Auth
    Cenario: Realizar o checkout completo
      Dado que adiciono lista de produtos ao carrinho de compras
        |id| produto                                          | quantidade | cor              | valor   |codeColor|
        |15| Beats Studio 2 Over-Ear Matte Black Headphones   | 1          | rgb(65, 65, 65)  | 179.99  |414141   |
        |30| HP Z4000 Wireless Mouse	                      | 2          | rgb(221, 58, 91) | 9.99    |DD3A5B   |
      Quando navego até o carrinho de compras
      Entao verifico que a lista de produtos foram adicionadas corretamente
        E confirmo o checkout dos produtos
        E confirmo os detalhes de entrega
        E confirmo os detalhes de pagamento
        E clico em pagar agora
        E verifico o resumo da compra

