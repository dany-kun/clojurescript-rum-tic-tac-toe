(ns tictac.setup)

(def side 3)
(def pieces {:player-1 "X" :player-2 "O" :empty "_"})
(def board (vec (repeat side
                        (vec (repeat side :empty)))))

(def current-player :player-1)
(def state {:board board :player current-player})
