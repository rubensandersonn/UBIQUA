import React, { useState } from "react";
import { CheckBox } from "react-native";
import { Wrapper, Texto, EditButton, Line } from "./style";
import { TextWhite, RoundButton } from "../../../utils/styled";

export default function Conta(props) {
  const {
    title,
    bill,
    whenChecked,
    handleButton,
    paid,
    textButton,
    styleButton
  } = props;
  const [state, setState] = useState({ checked: paid === true });

  return (
    <Wrapper>
      <CheckBox
        value={state.checked}
        onValueChange={() => {
          setState({ checked: !state.checked });

          // telling main when i was checked
          whenChecked();
        }}
      />
      <Line horizontal={true}>
        <Texto decoration={state.checked ? "line-through" : "none"}>
          R$ {bill ? bill : 0} -{" "}
        </Texto>
        <Texto decoration={state.checked ? "line-through" : "none"}>
          {title ? title : ""}
        </Texto>
      </Line>
      <RoundButton style={styleButton} onPress={handleButton}>
        <TextWhite style={{ fontWeight: "600" }}>{textButton}</TextWhite>
      </RoundButton>
    </Wrapper>
  );
}
