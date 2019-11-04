import { combineReducers, createStore } from "redux";

import { sensorReducer } from "./sensor";
import { perfilReducer } from "./perfil";

const Reducers = combineReducers({
  sensorState: sensorReducer,
  perfisState: perfilReducer
});

export const Store = createStore(Reducers);
