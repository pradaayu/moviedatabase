import React from "react";

export default function Hint(props: { msg: string, styles?: Partial<React.CSSProperties> }) {
	return <small style={{ color: "red", ...props.styles }}>{props.msg}</small>
}