import styled from "styled-components/native";
import { green, white, danger } from "./colors";

export const TextWhite = styled.Text`
  font-size: 12px;
  margin-left: 2%;
  color: ${white};
  padding: 2%;
`;

export const RoundButton = styled.TouchableOpacity`
  width: 40px;
  height: 40px;
  border-radius: 30px;
  background-color: ${danger};
  text-align: center;
  margin: 10px;
  padding: 10px;
  justify-content: center;
  align-items: center;
`;
