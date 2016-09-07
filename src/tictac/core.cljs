(ns tictac.core
  (:require [rum.core :as rum]
            [tictac.board.component :as board]
            [tictac.controls.component :as controls]))

(enable-console-print!)

(def container (delay (js/document.getElementById "board-container")))

(rum/defc tic-tac-toe []
  [:div
   (controls/c-winner)
   (board/c-board)
   (controls/c-controls)]
  )

(defn mount []
  (rum/mount (tic-tac-toe) @container))

(mount)