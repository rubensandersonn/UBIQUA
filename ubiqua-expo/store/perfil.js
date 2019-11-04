import { createStore } from "redux";
import { combineReducers } from "redux";

const roles = {
  ADM: "ADM",
  RESEARCHER: "RESEARCHER",
  PROFESSOR: "PROFESSOR"
};

const INITIAL_BILLS_STATE = [
  { id: 0, environment: "Room 1", name: "Rubens", role: roles.RESEARCHER }
];

const perfisActionTypes = {
  UPDATE: "UPDATE",
  UPDATE_ALL: "UPDATE_ALL",
  DELETE: "DELETE",
  ADD: "ADD"
};

// ACTIONS

export const actionUpdateAllPerfis = perfis => {
  return { type: perfisActionTypes.UPDATE_ALL, payload: { perfis: perfis } };
};

// actions for a single perfil

export const actionUpdatePerfil = (perfil, id) => {
  return {
    type: perfisActionTypes.UPDATE,
    payload: { perfil: perfil, id: id }
  };
};
export const actionDeletePerfil = id => ({
  type: perfisActionTypes.DELETE,
  payload: { id: id }
});
export const actionAddPerfil = perfil => {
  return {
    type: perfisActionTypes.ADD,
    payload: { perfil: perfil }
  };
};

// REDUCER

export const perfilReducer = (state = INITIAL_BILLS_STATE, action) => {
  switch (action.type) {
    case perfisActionTypes.UPDATE:
      return state.map(perfil =>
        perfil.id === action.payload.id ? action.payload.perfil : perfil
      );
    case perfisActionTypes.DELETE:
      return state.filter(perfil => perfil.id !== action.payload.id);

    case perfisActionTypes.ADD:
      return [...state, action.payload.perfil];
    case perfisActionTypes.UPDATE_ALL:
      return action.payload.perfis;
    default:
      return state;
  }
};

const Reducers = combineReducers({
  perfisState: perfilReducer
});

export const Store = createStore(Reducers);
