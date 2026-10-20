package br.com.ajufood.pedeai.util;

public enum StatusPedido {
    AGUARDANDO_CONFIRMACAO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    /**
     * Valida se transição de status esta sendo permitida
     */
    public boolean podeTransicionar(StatusPedido novoStatus) {
        // Estados finais não podem mudar
        if (this == ENTREGUE || this == CANCELADO) {
            return false;
        }

        // Qualquer status pode ir para CANCELADO (exceto ENTREGUE)
        if (novoStatus == CANCELADO) {
            return this != ENTREGUE;
        }

        // Fluxo linear obrigatório
        return switch (this) {
            case AGUARDANDO_CONFIRMACAO -> novoStatus == CONFIRMADO;
            case CONFIRMADO -> novoStatus == EM_PREPARO;
            case EM_PREPARO -> novoStatus == SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> novoStatus == ENTREGUE;
            default -> false;
        };
    }
}