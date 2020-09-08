(ns post-it.main
  (:gen-class)
  (:require [duct.core :as duct]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [duct-keys (or (duct/parse-keys args) [:duct/daemon])
        profiles  [:duct.profile/prod]]
    (-> (duct/resource "post_it/config.edn")
        (duct/read-config)
        (duct/exec-config profiles duct-keys))))
