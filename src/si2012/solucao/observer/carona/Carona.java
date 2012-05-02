package si2012.solucao.observer.carona;

import java.util.LinkedList;
import java.util.List;

public class Carona {

	private List<Usuario> pretendentes;
	private List<Usuario> confirmados;
	private boolean encerrada;
	private List<CaronaListener> listeners;

	public Carona() {
		pretendentes = new LinkedList<Usuario>();
		confirmados = new LinkedList<Usuario>();
		listeners = new LinkedList<CaronaListener>();
		encerrada = false;
	}

	public void solicitaCarona(Usuario usuario)
			throws SolicitacaoIlegalException {
		if (encerrada)
			throw new SolicitacaoIlegalException("Carona encerrada.");
		this.pretendentes.add(usuario);
		this.listeners.add(usuario);
	}

	public void confirmaCaroneiro(Usuario usuario) {
		if (pretendentes.remove(usuario)) {
			confirmados.add(usuario);
			usuario.caronaConfirmada(this);
		} else {
			throw new IllegalArgumentException("Caroneiro inexistente: "
					+ usuario);
		}
	}

	public void encerraCarona() {
		this.encerrada = true;
		for (Usuario pendente : pretendentes) {
			pendente.caronaNegada(this);
		}
		notificaListenersDeEncerramento();
	}

	private void notificaListenersDeEncerramento() {
		for (CaronaListener listener : listeners) {
			listener.notificaEncerramento(this);
		}
	}

	public void addListener(CaronaListener listener) {
		this.listeners.add(listener);
	}

	public boolean isConfirmado(Usuario usuario) {
		return confirmados.contains(usuario);
	}

}
