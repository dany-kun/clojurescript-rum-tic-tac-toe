(ns tictac.controls.component
  (:require [rum.core :as rum]
            [tictac.init :as setup]))

(rum/defc c-winner < rum/reactive []
  (let [winner (rum/react setup/game-over)]
    [:div {:class "win-message"}
     (when winner [:div (str "Player " (winner setup/pieces) " won!")])]))

(rum/defc c-controls []
  [:div {:on-click #(setup/restart!) :class "restart"} "Restart"])
