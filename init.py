#!/usr/bin/env python

import argparse
from pathlib import Path
from time import sleep
from typing import Callable
from urllib.request import Request, urlopen

import yaml

# Initialize Virtual environment
#   $ python -m venv .venv
#   $ source .venv/bin/activate
#   (.venv) pip install --upgrade pip
#   (.venv) pip install pyyaml


REQUEST_TIMEOUT_SEC = 30


def repeat_if(*exceptions: OSError, max_request_repeat: int = 30, repeat_sleep_sec: int = 10, print_info: bool = True):
    def repeat_if_wrapper(func: Callable):
        def wrapper(*args, **kwargs):
            args_str = ",".join([f"{str(val)}" for val in args])
            kwargs_str = ",".join([f"{key}={val}" for key, val in kwargs.items()])
            fn_str = f'#{func.__name__}({",".join([val for val in [args_str, kwargs_str] if val])})'

            happens = set()
            for rep in range(max_request_repeat):
                try:
                    return func(*args, **kwargs)
                except tuple(exceptions) as e:
                    step = rep + 1

                    happens.add(str(e))

                    if print_info:
                        repeat = f"{step}/{max_request_repeat}"
                        print(f"Waiting {repeat_sleep_sec} sec and repeat {repeat} for {fn_str}: {str(e)}")

                    sleep(repeat_sleep_sec)
            raise Exception(f'Fail with {fn_str}: {",".join(happens)}')

        return wrapper

    return repeat_if_wrapper


REST_KEY = "rest"
TABLES_KEY = "tables"


def xml_tag(name: str, internal_tags: list[str], attr: dict[str, str]) -> str:
    attr_str = " ".join([f'{key}="{value}"' for key, value in attr.items()])
    content = "\n".join(internal_tags)
    return f"<{name} {attr_str}>{content}</{name}>"


def xml(root: str) -> str:
    return f'<?xml version="1.0" encoding="UTF-8"?>{root}'


# See:
#   https://hbase.apache.org/book.html#_rest
@repeat_if(OSError)
def add_hbase_table(rest: str, tables: list[str]) -> None:
    with urlopen(f"http://{rest}", timeout=REQUEST_TIMEOUT_SEC) as resp:
        existing_tables = [s.strip() for s in resp.read().decode().split("\n") if s]

    tables_def = []
    for table_def in tables:
        def_blocks = table_def.split(",")
        table_name = def_blocks[0].strip()
        table_family = [s.strip() for s in def_blocks[1:]]
        tables_def.append((table_name, table_family))

    create_tables = [td for td in tables_def if td[0] not in existing_tables]

    for table_name, table_family in create_tables:
        families_tags = []
        for family in table_family:
            families_tags.append(xml_tag("ColumnSchema", [], {"name": family}))
        table_tag = xml_tag("TableSchema", families_tags, {"name": table_name})
        post = Request(
            f"http://{rest}/{table_name}/schema",
            headers={
                "Accept": "text/xml",
                "Content-Type": "text/xml",
            },
            data=xml(table_tag).encode(),
        )
        with urlopen(post, timeout=REQUEST_TIMEOUT_SEC):
            print(f"Add table '{table_name}' with family '{','.join(table_family)}'")

    print("HBase have all required Tables")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(prog="CUP-Init", description="CUP cluster initialization script")
    parser.add_argument("-c", "--config", nargs="?", type=Path, help="Config file", default="init.yml")
    args = parser.parse_args()

    with open(args.config, "r", encoding="UTF-8") as file:
        config = yaml.safe_load(file)

        try:
            add_hbase_table(config.get(REST_KEY), config.get(TABLES_KEY))
        except Exception as e:
            print(f"Can't initialize cluster! Exception '{type(e)}': {str(e)}")
